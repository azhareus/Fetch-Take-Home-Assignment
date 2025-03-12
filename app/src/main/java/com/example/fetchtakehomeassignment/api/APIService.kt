package com.example.fetchtakehomeassignment.api

import com.example.fetchtakehomeassignment.model.ApiResult
import retrofit2.http.GET

interface APIService {

    @GET("hiring.json")
    suspend fun getApiResults():List<ApiResult>
}