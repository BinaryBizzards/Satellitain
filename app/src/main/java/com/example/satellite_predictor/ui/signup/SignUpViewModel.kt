package com.example.satellite_predictor.ui.signup

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

class SignUpViewModel : ViewModel() {

    private val _SignUpStatus = MutableLiveData<String?>(null)
    val SignUpStatus: LiveData<String?>
        get() = _SignUpStatus

    private val _isRegistered = MutableLiveData(false)
    val isRegistered: LiveData<Boolean>
        get() = _isRegistered

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _errorMessage = MutableLiveData<String?>(null)
    val errorMessage: LiveData<String?>
        get() = _errorMessage

    fun signup(name: String, email: String, password: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val map: MutableMap<String, RequestBody> = mutableMapOf()
                map.put("name",createPartFromString(name))
                map.put("email",createPartFromString(email))
                map.put("password",createPartFromString(password))
                val result = RetrofitInstance.api.signup(map)
                _errorMessage.value = null
                _isRegistered.value = true
                _SignUpStatus.value = result
                Log.i("SignUpViewModel", result)
            } catch (e: Exception) {
                if (e is SocketTimeoutException || e is IOException) {
                    _errorMessage.value = "Check your Network Connection!"
                } else _errorMessage.value = e.message
                _isRegistered.value = false
            } finally {
                _isLoading.value = false
            }

        }
    }

    fun createPartFromString(stringData: String): RequestBody {
        return RequestBody.create(MediaType.parse("text/plain"), stringData)
    }
}