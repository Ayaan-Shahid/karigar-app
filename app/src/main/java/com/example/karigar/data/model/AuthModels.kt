package com.example.karigar.data.model

import com.google.gson.annotations.SerializedName

// Request body for requesting OTP
data class OtpRequest(
    @SerializedName("phone_number")
    val phoneNumber: String
)

// Response body from the server
data class OtpResponse(
    val message: String
)