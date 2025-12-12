package com.example.karigar.navigation

// 1. Make sure to import your new Dashboard Screen
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.karigar.ui.auth.LoginSignupScreen
import com.example.karigar.ui.auth.SignupScreen
import com.example.karigar.ui.auth.UserRole
import com.example.karigar.ui.customer.CustomerDashboardScreen
import com.example.karigar.ui.customer.SelectServiceScreen
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
                onLoginClick = {navController.navigate("CustomerDashboard")},
                // This will now work because "CustomerDashboard" is defined below
                onSignupClick = { navController.navigate("SignUp") }
            )
        }

        composable("SignUp") {
            SignupScreen(
                onVerificationSuccess = { roleName: String, phone: String ->
                    val role = try {
                        UserRole.valueOf(roleName)
                    } catch (e: Exception) {
                        UserRole.CUSTOMER
                    }
                    println("Signup Success: $role with $phone")

                    // You probably want to navigate here too eventually
                    // navController.navigate("CustomerDashboard")
                }
            )
        }

        // 2. ADD THIS BLOCK to define the destination
        composable("CustomerDashboard") {
            CustomerDashboardScreen(
                onPostRequestClick = {
                    navController.navigate("SelectService")
                }
            )
        }

        composable("SelectService"){
            SelectServiceScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onNextClick = { selectedCategory ->
                    println("Selected Category: $selectedCategory")
                }
            )
        }
    }
}