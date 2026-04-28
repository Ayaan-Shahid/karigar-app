package com.example.karigar.ui.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.karigar.data.api.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream

// State to track the API submission process
sealed class SubmissionState {
    object Idle : SubmissionState()
    object Loading : SubmissionState()
    object Success : SubmissionState()
    data class Error(val message: String) : SubmissionState()
}

data class PostRequestState(
    val serviceCategory: String = "",
    val description: String = "",
    val imageUri: String? = null,
    val price: String = "",
    val address: String = "F-8 Markaz, Islamabad",
    val houseNo: String = "",
    val scheduledDate: String = "Tomorrow, 2:00 PM",
    val isEditMode: Boolean = false
)

class PostRequestViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(PostRequestState())
    val uiState = _uiState.asStateFlow()

    // --- NEW: Submission Status State ---
    private val _submissionStatus = MutableStateFlow<SubmissionState>(SubmissionState.Idle)
    val submissionStatus = _submissionStatus.asStateFlow()

    // --- Existing UI Updaters ---

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

    fun updateHouseNo(input: String) {
        _uiState.update { it.copy(houseNo = input) }
    }

    // --- NEW: Submit Job Function ---

    fun submitJob(context: Context) {
        viewModelScope.launch {
            _submissionStatus.value = SubmissionState.Loading

            try {
                val currentState = _uiState.value

                // 1. Prepare Text Fields (Convert Strings to RequestBody)
                val serviceTypePart = createPartFromString(currentState.serviceCategory)
                val descriptionPart = createPartFromString(currentState.description)
                val budgetPart = createPartFromString(currentState.price)

                // Combine address and houseNo for the full address
                val fullAddress = if (currentState.houseNo.isNotEmpty()) {
                    "${currentState.address}, ${currentState.houseNo}"
                } else {
                    currentState.address
                }
                val addressPart = createPartFromString(fullAddress)

                // 2. Prepare Image Part (Convert URI to MultipartBody.Part)
                var imagePart: MultipartBody.Part? = null
                currentState.imageUri?.let { uriString ->
                    val uri = Uri.parse(uriString)
                    val file = getFileFromUri(context, uri)
                    if (file != null) {
                        // Create RequestBody for the file
                        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                        // "image" is the key name; change if backend expects "file"
                        imagePart = MultipartBody.Part.createFormData("image", file.name, requestFile)
                    }
                }

                // 3. Make the API Call
                val response = RetrofitClient.apiService.createJob(
                    serviceType = serviceTypePart,
                    description = descriptionPart,
                    budget = budgetPart,
                    address = addressPart,
                    image = imagePart
                )

                if (response.isSuccessful) {
                    Log.d("API", "Success: ${response.body()}")
                    _submissionStatus.value = SubmissionState.Success
                } else {
                    val errorMsg = response.errorBody()?.string() ?: "Unknown error"
                    Log.e("API", "Error: $errorMsg")
                    _submissionStatus.value = SubmissionState.Error("Failed: ${response.code()}")
                }

            } catch (e: Exception) {
                Log.e("API", "Exception", e)
                _submissionStatus.value = SubmissionState.Error(e.message ?: "Connection Error")
            }
        }
    }
    // --- Helper Functions ---

    private fun createPartFromString(value: String): RequestBody {
        return value.toRequestBody("text/plain".toMediaTypeOrNull())
    }

    // Helper to turn a Content URI (content://...) into a temporary File
    private fun getFileFromUri(context: Context, uri: Uri): File? {
        return try {
            val contentResolver = context.contentResolver
            val inputStream = contentResolver.openInputStream(uri) ?: return null
            // Create a temp file in the app's cache directory
            val file = File.createTempFile("upload", ".jpg", context.cacheDir)
            val outputStream = FileOutputStream(file)
            inputStream.copyTo(outputStream)
            inputStream.close()
            outputStream.close()
            file
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    fun setEditMode(isEnabled: Boolean) {
        _uiState.update { it.copy(isEditMode = isEnabled) }
    }

    // Ensure we reset edit mode when submitting or clearing
    fun resetSubmissionStatus() {
        _submissionStatus.value = SubmissionState.Idle
        setEditMode(false) // Reset edit mode when finished
    }
}