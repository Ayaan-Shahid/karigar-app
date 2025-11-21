package com.example.karigar.ui.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Handyman
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

enum class UserRole { CUSTOMER, TECHNICIAN }

@Preview
@Composable
fun SignupScreen(
    onVerificationSuccess: (UserRole, String) -> Unit = { _, _ -> }
) {
    val primary = Color(0xFF4CAF50)
    val textDark = Color(0xFF212121)
    val subtle = Color(0xFF4F4F4F)
    val borderLight = Color(0xFFE0E0E0)

    var selectedRole by remember { mutableStateOf(UserRole.CUSTOMER) }
    var phoneNumber by remember { mutableStateOf("") }

    var showOtpSection by remember { mutableStateOf(false) }

    // OTP Inputs
    var otp1 by remember { mutableStateOf("") }
    var otp2 by remember { mutableStateOf("") }
    var otp3 by remember { mutableStateOf("") }
    var otp4 by remember { mutableStateOf("") }

    // Countdown Timer
    var counter by remember { mutableStateOf(30) }
    var canResend by remember { mutableStateOf(false) }

    // Start countdown when OTP section becomes visible
    LaunchedEffect(showOtpSection) {
        if (showOtpSection) {
            counter = 30
            canResend = false
            while (counter > 0) {
                delay(1000)
                counter--
            }
            canResend = true
        }
    }

    fun isValidPakistaniNumber(num: String): Boolean {
        return num.length == 10 && num.startsWith("3")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F9FC))
            .padding(top = 65.dp, bottom = 45.dp, start = 24.dp, end = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(10.dp))

        // App Logo
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .background(
                        color = Color(0x334CAF50), // primary with 20% opacity (#4CAF50/20)
                        shape = RoundedCornerShape(20.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Handyman,
                    contentDescription = "Handyman Icon",
                    tint = primary,
                    modifier = Modifier.size(45.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text("Welcome", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = textDark)

            Text(
                "Create an account to continue",
                fontSize = 15.sp,
                color = subtle,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 10.dp)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // ROLE SELECTOR
        Column(modifier = Modifier.fillMaxWidth()) {

            Text(
                "Select Your Role",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = textDark
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier
                    .width(180.dp)
                    .height(48.dp)
                    .background(Color(0xFFE0E0E0).copy(alpha = 0.5f), RoundedCornerShape(14.dp))
                    .padding(4.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {

                RoleOption(
                    label = "Customer",
                    selected = selectedRole == UserRole.CUSTOMER,
                    onClick = { selectedRole = UserRole.CUSTOMER }
                )

                RoleOption(
                    label = "Technician",
                    selected = selectedRole == UserRole.TECHNICIAN,
                    onClick = { selectedRole = UserRole.TECHNICIAN }
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // PHONE INPUT SECTION — Visible only before OTP
        AnimatedVisibility(
            visible = !showOtpSection,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Column {

                Text(
                    "Phone Number",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = textDark
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = phoneNumber,
                    onValueChange = { input ->
// Remove any non-digit characters
                        val digits = input.filter { it.isDigit() }

                        // Limit to max 10 digits (without country code)
                        phoneNumber = digits.take(10)

                        // Format according to Pakistan mobile number style: 3XX XXXXXXX
//                        phoneNumber = when {
//                            limited.length <= 3 -> limited
//                            else -> "${limited.substring(0,3)} ${limited.substring(3)}"
//                        }
                    },
                    placeholder = { Text("3001234567") },
                    leadingIcon = { Text("+92 ", fontSize = 16.sp, fontWeight = FontWeight.Medium) },
                    singleLine = true,
                    textStyle = TextStyle(fontSize = 17.sp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)

                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { showOtpSection = true },
                    enabled = isValidPakistaniNumber(phoneNumber),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = primary,
                        contentColor = Color.White
                    )
                ) {
                    Text("Send Code", fontSize = 17.sp)
                }
            }
        }

        // OTP SECTION — Visible only after valid number
        AnimatedVisibility(
            visible = showOtpSection,
            enter = fadeIn(),
            exit = fadeOut()
        ) {

            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    "Enter the 4-digit code sent to your number.",
                    fontSize = 15.sp,
                    color = subtle,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OtpBox(value = otp1) { otp1 = it }
                    Spacer(modifier = Modifier.width(8.dp))
                    OtpBox(value = otp2) { otp2 = it }
                    Spacer(modifier = Modifier.width(8.dp))
                    OtpBox(value = otp3) { otp3 = it }
                    Spacer(modifier = Modifier.width(8.dp))
                    OtpBox(value = otp4) { otp4 = it }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        onVerificationSuccess(selectedRole, phoneNumber)
                    },
                    enabled = otp1.isNotEmpty() && otp2.isNotEmpty() &&
                            otp3.isNotEmpty() && otp4.isNotEmpty(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = primary,
                        contentColor = Color.White
                    )
                ) {
                    Text("Verify & Continue", fontSize = 17.sp)
                }

                Spacer(modifier = Modifier.height(10.dp))

                if (!canResend) {
                    Text(
                        "Didn't receive code? Resend in 0:${counter.toString().padStart(2, '0')}",
                        fontSize = 14.sp,
                        color = subtle
                    )
                } else {
                    Text(
                        "Resend Code",
                        fontSize = 14.sp,
                        color = primary,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.clickable {
                            showOtpSection = false
                            phoneNumber = ""
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            "By continuing, you agree to our Terms of Service and Privacy Policy.",
            fontSize = 11.sp,
            color = subtle,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun RoleOption(label: String, selected: Boolean, onClick: () -> Unit) {
    val bg = if (selected) Color.White else Color.Transparent
    val textColor = if (selected) Color(0xFF212121) else Color(0xFF4F4F4F)

    Box(
        modifier = Modifier
            .fillMaxHeight()
            .clip(RoundedCornerShape(12.dp))
            .background(bg)
            .border(
                width = if (selected) 1.dp else 0.dp,
                color = if (selected) Color(0xFFE0E0E0) else Color.Transparent,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            label,
            color = textColor,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(6.dp)
        )
    }
}

@Composable
fun OtpBox(value: String, onValueChange: (String) -> Unit) {
    Box(
        modifier = Modifier
            .size(56.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFF7F9FC))
            .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(12.dp)),
        contentAlignment = Alignment.Center
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = {
                if (it.length <= 1 && it.all { char -> char.isDigit() })
                    onValueChange(it)
            },
            textStyle = TextStyle(fontSize = 22.sp, textAlign = TextAlign.Center),
            singleLine = true,
            modifier = Modifier.fillMaxSize(),
//            colors = TextFieldDefaults.outlinedTextFieldColors(
//                focusedBorderColor = Color(0xFF4CAF50),
//                unfocusedBorderColor = Color.Transparent,
//                containerColor = Color.Transparent
//            )
        )
    }
}
