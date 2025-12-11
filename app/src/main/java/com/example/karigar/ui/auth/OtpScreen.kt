package com.example.karigar.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun OtpScreen(
    phoneNumber: String,
    userRole: UserRole,
    onVerificationSuccess: (UserRole, String) -> Unit,
    onBackClick: () -> Unit
) {
    val primary = Color(0xFF4CAF50)
    val textDark = Color(0xFF212121)
    val subtle = Color(0xFF757575)

    // State for the OTP (Single string, max 4 chars)
    var otpValue by remember { mutableStateOf("") }

    // Timer State
    var counter by remember { mutableIntStateOf(30) }
    var canResend by remember { mutableStateOf(false) }

    // Start Timer
    LaunchedEffect(Unit) {
        counter = 30
        canResend = false
        while (counter > 0) {
            delay(1000)
            counter--
        }
        canResend = true
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F9FC))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top Bar with Back Button
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.CenterStart
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                modifier = Modifier
                    .size(24.dp)
                    .clickable { onBackClick() },
                tint = textDark
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        Text(
            "Verify Phone Number",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = textDark
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            "We've sent a code to +92 $phoneNumber",
            fontSize = 15.sp,
            color = subtle,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        // REAL-WORLD OTP INPUT
        // We use a DecorationBox pattern here for perfect focus handling
        BasicTextField(
            value = otpValue,
            onValueChange = {
                if (it.length <= 4 && it.all { char -> char.isDigit() }) {
                    otpValue = it
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
            decorationBox = {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(4) { index ->
                        val char = when {
                            index < otpValue.length -> otpValue[index].toString()
                            else -> ""
                        }

                        // Highlight the box that is currently waiting for input
                        val isFocused = index == otpValue.length

                        OtpDigitBox(
                            char = char,
                            isFocused = isFocused
                        )

                        if (index < 3) Spacer(modifier = Modifier.width(12.dp))
                    }
                }
            }
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                onVerificationSuccess(userRole, phoneNumber)
            },
            enabled = otpValue.length == 4,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = primary,
                contentColor = Color.White,
                disabledContainerColor = Color(0xFFE0E0E0),
                disabledContentColor = Color(0xFF9E9E9E)
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Verify & Continue", fontSize = 17.sp)
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (!canResend) {
            Text(
                "Resend code in 0:${counter.toString().padStart(2, '0')}",
                fontSize = 14.sp,
                color = subtle
            )
        } else {
            Text(
                "Resend Code",
                fontSize = 15.sp,
                color = primary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable {
                    // Reset timer
                    counter = 30
                    canResend = false
                    // Reset OTP logic here if needed (e.g., API call)
                }
            )
        }
    }
}

@Composable
fun OtpDigitBox(char: String, isFocused: Boolean) {
    val borderColor = if (isFocused) Color(0xFF4CAF50) else Color(0xFFE0E0E0)
    val borderWidth = if (isFocused) 2.dp else 1.dp

    Box(
        modifier = Modifier
            .size(60.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .border(borderWidth, borderColor, RoundedCornerShape(12.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = char,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF212121)
        )
    }
}

@Preview
@Composable
fun OtpScreenPreview(){
    OtpScreen(
        phoneNumber = "3137433771",
        userRole = UserRole.CUSTOMER,
        onVerificationSuccess = { _, _ ->},
        onBackClick = {}
    )
}