package com.example.karigar.ui.auth


import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.karigar.R
import com.example.karigar.ui.viewmodel.AuthState
import com.example.karigar.ui.viewmodel.SignUpViewModel
import com.joelkanyi.jcomposecountrycodepicker.component.KomposeCountryCodePicker
import com.joelkanyi.jcomposecountrycodepicker.component.rememberKomposeCountryCodePickerState


enum class SignupStep { PHONE_INPUT, OTP_VERIFICATION }

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SignupScreen(
    onVerificationSuccess: (String, String) -> Unit,
    viewModel: SignUpViewModel = viewModel()
) {
    val context = LocalContext.current
    val authState by viewModel.authState.collectAsState()

    var currentStep by remember { mutableStateOf(SignupStep.PHONE_INPUT) }
    var selectedRole by remember { mutableStateOf(UserRole.CUSTOMER) }
    val pickerState = rememberKomposeCountryCodePickerState("PK")
    var rawInput by remember { mutableStateOf("") }

    val primary = Color(0xFF28A745)
    val textDark = Color(0xFF212121)
    val subtle = Color(0xFF4F4F4F)

    // Observe API Result
    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Success -> {
                // Only move to OTP screen when the API confirms success
                currentStep = SignupStep.OTP_VERIFICATION
                viewModel.resetState()
            }
            is AuthState.Error -> {
                Toast.makeText(context, (authState as AuthState.Error).message, Toast.LENGTH_LONG).show()
                viewModel.resetState()
            }
            else -> {}
        }
    }

    AnimatedContent(
        targetState = currentStep,
        transitionSpec = {
            if (targetState == SignupStep.OTP_VERIFICATION) {
                (slideInHorizontally { width -> width } + fadeIn()).togetherWith(
                    slideOutHorizontally { width -> -width } + fadeOut())
            } else {
                (slideInHorizontally { width -> -width } + fadeIn()).togetherWith(
                    slideOutHorizontally { width -> width } + fadeOut())
            }
        },
        label = "ScreenTransition"
    ) { step ->
        when (step) {
            SignupStep.OTP_VERIFICATION -> {
                OtpScreen(
                    // Pass the full number with country code to the next screen
                    phoneNumber = pickerState.getCountryPhoneCodeWithoutPrefix() + pickerState.phoneNumber,
                    userRole = selectedRole,
                    onVerificationSuccess = { role, phone ->
                        onVerificationSuccess(role.name, phone)
                    },
                    onBackClick = { currentStep = SignupStep.PHONE_INPUT }
                )
            }

            SignupStep.PHONE_INPUT -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFFF7F9FC))
                        .padding(top = 65.dp, bottom = 45.dp, start = 24.dp, end = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // --- LOGO & WELCOME SECTION ---
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .size(90.dp)
                                .background(Color(0x334CAF50), RoundedCornerShape(20.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.app_logo),
                                contentDescription = "App logo",
                                tint = primary,
                                modifier = Modifier.size(70.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                        Text("Welcome", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = textDark)
                        Text("Create an account to continue", fontSize = 15.sp, color = subtle, textAlign = TextAlign.Center)
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // --- ROLE SELECTOR ---
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text("Select Your Role", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = textDark)
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier
                                .width(200.dp)
                                .height(48.dp)
                                .background(Color(0xFFE0E0E0).copy(alpha = 0.5f), RoundedCornerShape(14.dp))
                                .padding(4.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            RoleOption("Customer", selectedRole == UserRole.CUSTOMER) { selectedRole = UserRole.CUSTOMER }
                            RoleOption("Technician", selectedRole == UserRole.TECHNICIAN) { selectedRole = UserRole.TECHNICIAN }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // --- PHONE INPUT ---
                    Column {
                        Text("Phone Number", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = textDark)
                        Spacer(modifier = Modifier.height(8.dp))

                        KomposeCountryCodePicker(
                            modifier = Modifier.fillMaxWidth().height(56.dp),
                            state = pickerState,
                            text = rawInput,
                            onValueChange = { input ->
                                rawInput = input
                                pickerState.phoneNumber = input
                            },
                            shape = RoundedCornerShape(12.dp)
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        // --- UPDATED BUTTON WITH LOADING STATE ---
                        Button(
                            onClick = {
                                // Construct full number (e.g., 923137433771)
                                val fullNumber = pickerState.getCountryPhoneCodeWithoutPrefix() + pickerState.phoneNumber
                                viewModel.sendOtp(fullNumber)
                            },
                            // Disable button if invalid OR if currently loading
                            enabled = pickerState.isPhoneNumberValid() && authState !is AuthState.Loading,
                            modifier = Modifier.fillMaxWidth().height(56.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = primary,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            if (authState is AuthState.Loading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    color = Color.White,
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Text("Send Code", fontSize = 17.sp)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        "By continuing, you agree to our Terms of Service.",
                        fontSize = 11.sp,
                        color = subtle,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
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
                if (selected) 1.dp else 0.dp,
                if (selected) Color(0xFFE0E0E0) else Color.Transparent,
                RoundedCornerShape(12.dp)
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            label,
            color = textColor,
            maxLines = 1,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 12.dp)
        )
    }
}

@Preview
@Composable
fun SignUpScreenPreview() {
    SignupScreen(
        onVerificationSuccess = { role, phone -> println("Preview Success: Role=$role, Phone=$phone") }
    )
}