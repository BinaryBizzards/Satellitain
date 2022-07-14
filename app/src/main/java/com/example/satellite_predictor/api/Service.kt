package com.example.satellite_predictor.api

import com.example.satellite_predictor.models.PredictionResult
import com.example.satellite_predictor.models.Satellite
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*
import java.io.File


interface Service {

    @Multipart
    @POST("signup")
    suspend fun signup(
        @PartMap() partMap: MutableMap<String, RequestBody>
    ): String

    @Multipart
    @POST("login")
    suspend fun login(
        @PartMap() partMap: MutableMap<String, RequestBody>
    ): String

    @GET("logout")
    suspend fun logout(): String

    @GET("get_list")
    suspend fun getList(
        @Query("_page") page: Int = 1,
        @Query("_limit") limit: Int = 10
    ): List<Satellite>

    @Multipart
    @POST("predict")
    suspend fun predict(
        /* @Part("location") location: RequestBody,*/
        @Part file: MultipartBody.Part
    ): PredictionResult
}