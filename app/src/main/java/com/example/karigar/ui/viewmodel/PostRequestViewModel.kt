package com.example.karigar.ui.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class PostRequestState(
    val serviceCategory: String = "",
    val description: String = "",
    val imageUri: String? = null,
    val price: String = "",
    val address: String = "F-8 Markaz, Islamabad",
    val houseNo: String = "",
    val scheduledDate: String = "Tomorrow, 2:00 PM"
)

class PostRequestViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(PostRequestState())
    val uiState = _uiState.asStateFlow()

    fun updateCategory(category: String) {
        _uiState.update { it.copy(serviceCategory = category) }
    }

    fun updateIssueDetails(description: String, uri: String?) {
        _uiState.update { it.copy(description = description, imageUri = uri) }
    }

    fun updatePrice(price: String) {
        _uiState.update { it.copy(price = price) }
    }

    fun updateLocation(address: String) {
        _uiState.update { it.copy(address = address) }
    }

    // <--- ADD THIS FUNCTION
    fun updateHouseNo(input: String) {
        _uiState.update { it.copy(houseNo = input) }
    }
}