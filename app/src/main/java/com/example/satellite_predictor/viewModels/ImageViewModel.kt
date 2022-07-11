package com.example.satellite_predictor.viewModels

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.satellite_predictor.api.RetrofitInstance
import com.example.satellite_predictor.models.PredictionResult
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

private val TAG = "ImageViewModel"

class ImageViewModel : ViewModel() {

    private val _result: MutableLiveData<PredictionResult> = MutableLiveData()
    val result: LiveData<PredictionResult>
        get() = _result

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    fun predict(file: File) {
//        val location_details: RequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), "location")
//        take location as input
        _isLoading.value = true
        val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)
        val image = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
        viewModelScope.launch {
            try {
                val predicted_result = RetrofitInstance.api.predict(image)
                _result.value = predicted_result
            } catch (e: Exception) {
                Log.e(TAG, e.message.toString())
            } finally {
                _isLoading.value = false;
            }
        }
    }
}