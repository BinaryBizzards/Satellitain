package com.example.satellite_predictor.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.satellite_predictor.api.RetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.Retrofit

class LogoutViewModel : ViewModel() {
    private val _isLoggedOut = MutableLiveData(false)
    val isLoggedOut: LiveData<Boolean>
        get() = _isLoggedOut

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _errorMessage = MutableLiveData<String?>(null)
    val errorMessage: LiveData<String?>
        get() = _errorMessage

    fun logout() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                RetrofitInstance.api.logout()
                _isLoggedOut.value = true
                _errorMessage.value = null
            } catch (e: Exception) {
                Log.i("LogoutViewModel", e.toString())
                _errorMessage.value = e.toString()
                _isLoggedOut.value = false
            } finally {
                _isLoading.value = false
            }
        }
    }
}