package com.example.karigar.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.karigar.ui.auth.LoginSignupScreen
import com.example.karigar.ui.auth.SignupScreen
import com.example.karigar.ui.auth.UserRole
import com.example.karigar.ui.onboarding.OnboardingScreenFirst

@Composable
fun KarigarNavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "onboarding"
    ) {

        composable("onboarding") {
            OnboardingScreenFirst(
                onContinueToLogin = {
                    navController.navigate("loginSignup") { popUpTo("onboarding") { inclusive = true } }
                },
                onSkipToLogin = {
                    navController.navigate("loginSignup") { popUpTo("onboarding") { inclusive = true } }
                }
            )
        }

        composable("loginSignup") {
            LoginSignupScreen(
                onLoginClick = { /* TODO: Login */ },
                onSignupClick = { navController.navigate("SignUp") }
            )
        }

        composable("SignUp") {
            SignupScreen(
                // FIX: Receive String, convert back to Enum
                onVerificationSuccess = { roleName: String, phone: String ->

                    val role = try {
                        UserRole.valueOf(roleName)
                    } catch (e: Exception) {
                        UserRole.CUSTOMER
                    }

                    println("Signup Success: $role with $phone")

                    // Example: Navigate to Home
                    // navController.navigate("home")
                }
            )
        }
    }
}