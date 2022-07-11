package com.example.satellite_predictor.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.satellite_predictor.api.RetrofitInstance
import com.example.satellite_predictor.models.Satellite
import kotlinx.coroutines.launch

private const val TAG = "MainViewModel"

class MainViewModel : ViewModel() {
    private val _satelliteList: MutableLiveData<List<Satellite>> = MutableLiveData()
    val satelliteList: LiveData<List<Satellite>>
        get() = _satelliteList

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _errorMessage = MutableLiveData<String?>(null)
    val errorMessage: LiveData<String?>
        get() = _errorMessage

    private var currentPage = 1

    fun getList() {
        viewModelScope.launch {
            Log.i(TAG, "Query with Page $currentPage")
            _errorMessage.value = null
            _isLoading.value = true
            try {
                val fetchedList = RetrofitInstance.api.getList(currentPage)
                currentPage += 1;
                val currentList = satelliteList.value ?: emptyList()
                _satelliteList.value = currentList + fetchedList
            } catch (e: Exception) {
                _errorMessage.value = e.message
                Log.e(TAG, "Exception ${e}")
            } finally {
                _isLoading.value = false
            }
        }
    }
}