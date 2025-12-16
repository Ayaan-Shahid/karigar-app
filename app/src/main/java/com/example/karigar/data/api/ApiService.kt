package com.example.karigar.data.api

import com.example.karigar.data.model.ChatRequest
import com.example.karigar.data.model.ChatResponse
import com.example.karigar.data.model.JobResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {
    @POST("api/v1/ai_chatbot/")
    suspend fun chatWithAi(@Body request: ChatRequest): Response<ChatResponse>

    @Multipart
    @POST("api/v1/jobs/")
    suspend fun createJob(
        @Part("service_type") serviceType: RequestBody,
        @Part("description") description: RequestBody,
        @Part("budget") budget: RequestBody,
        @Part("address") address: RequestBody,
        // The image is optional (nullable)
        @Part image: MultipartBody.Part?
    ): Response<JobResponse>

}