package com.example.fetchtakehomeassignment.repository

import com.example.fetchtakehomeassignment.api.APIService
import com.example.fetchtakehomeassignment.model.ApiResult
import javax.inject.Inject

class ApiRepository @Inject constructor(private val apiService: APIService) {

    suspend fun fetchResults(): List<ApiResult> {
        return apiService.getApiResults()
    }
}