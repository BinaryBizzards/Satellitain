package com.example.satellite_predictor.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.satellite_predictor.api.RetrofitInstance
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.RequestBody
import java.io.IOException
import java.net.SocketTimeoutException

class LoginViewModel : ViewModel() {
    private val _LoginStatus = MutableLiveData<String?>(null)
    val LoginStatus: LiveData<String?>
        get() = _LoginStatus

    private val _isLoggedIn = MutableLiveData(false)
    val isLoggedIn: LiveData<Boolean>
        get() = _isLoggedIn

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _errorMessage = MutableLiveData<String?>(null)
    val errorMessage: LiveData<String?>
        get() = _errorMessage

    fun login(email: String, password: String, remember: Boolean) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val map: MutableMap<String, RequestBody> = mutableMapOf()
                map.put("email",createPartFromString(email))
                map.put("password",createPartFromString(password))
                map.put("remember",createPartFromString(remember.toString()))
                val result = RetrofitInstance.api.login(map)
                _errorMessage.value = null
                _isLoggedIn.value = true
                _LoginStatus.value=result
            } catch (e: Exception) {
                if (e is SocketTimeoutException || e is IOException) {
                    _errorMessage.value = "Check your Network Connection!"
                } else _errorMessage.value = e.message
                _isLoggedIn.value = false
            } finally {
                _isLoading.value = false
            }
        }

    }
    fun createPartFromString(stringData: String): RequestBody {
        return RequestBody.create(MediaType.parse("text/plain"), stringData)
    }

}