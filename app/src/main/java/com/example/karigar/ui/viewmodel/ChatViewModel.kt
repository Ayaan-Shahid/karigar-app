package com.example.karigar.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.karigar.data.api.RetrofitClient
import com.example.karigar.data.model.ChatRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// Simple data class for UI list
data class ChatMessage(val text: String, val isUser: Boolean)

class ChatViewModel : ViewModel() {

    // Holds the list of messages (Conversation history)
    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages = _messages.asStateFlow()

    // Loading state
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    fun sendMessage(userText: String) {
        if (userText.isBlank()) return

        // 1. Add User Message to List immediately
        val currentList = _messages.value.toMutableList()
        currentList.add(ChatMessage(userText, true))
        _messages.value = currentList
        _isLoading.value = true

        viewModelScope.launch {
            try {
                // 2. Call the API
                val response = RetrofitClient.apiService.chatWithAi(ChatRequest(userText))

                if (response.isSuccessful && response.body() != null) {
                    // 3. Add AI Response to List
                    val aiReply = response.body()!!.reply
                    val updatedList = _messages.value.toMutableList()
                    updatedList.add(ChatMessage(aiReply, false))
                    _messages.value = updatedList
                } else {
                    // Handle API error
                    addSystemMessage("Error: AI could not respond.")
                }
            } catch (e: Exception) {
                // Handle Network error
                addSystemMessage("Connection failed: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun addSystemMessage(text: String) {
        val updatedList = _messages.value.toMutableList()
        updatedList.add(ChatMessage(text, false))
        _messages.value = updatedList
    }
}