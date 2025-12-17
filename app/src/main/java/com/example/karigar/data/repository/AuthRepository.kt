package com.example.karigar.data.repository

import com.example.karigar.data.api.RetrofitClient
import com.example.karigar.data.model.OtpRequest
import com.example.karigar.data.model.OtpResponse
import retrofit2.Response

class AuthRepository {
    private val apiService = RetrofitClient.apiService

    suspend fun requestOtp(phoneNumber: String): Response<OtpResponse> {
        return apiService.requestOtp(OtpRequest(phoneNumber))
    }
}