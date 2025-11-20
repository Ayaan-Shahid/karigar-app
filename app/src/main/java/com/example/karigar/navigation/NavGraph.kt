package com.example.karigar.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.karigar.ui.auth.LoginSignupScreen
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
                onContinueToLogin = { navController.navigate("loginSignup") },
                onSkipToLogin = { navController.navigate("loginSignup") }
            )
        }

        composable("loginSignup") {
            LoginSignupScreen(
                onLoginClick = {
                    // TODO: navigate to your login screen
                },
                onSignupClick = {
                    // TODO: navigate to your signup screen
                }
            )
        }
    }
}
