package com.example.karigar.data.api

import com.example.karigar.data.model.ChatRequest
import com.example.karigar.data.model.ChatResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("api/v1/ai_chatbot/")
    suspend fun chatWithAi(@Body request: ChatRequest): Response<ChatResponse>
}