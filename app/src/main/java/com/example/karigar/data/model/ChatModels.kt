package com.example.karigar.data.model

// What you SEND to the AI
data class ChatRequest(
    val message: String
)

// What the AI sends BACK
data class ChatResponse(
    val reply: String // Check if your backend sends "response", "message", or "answer"
)