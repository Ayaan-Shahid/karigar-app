package com.example.karigar.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.example.karigar.ui.auth.LoginSignupScreen
import com.example.karigar.ui.auth.SignupScreen
import com.example.karigar.ui.auth.UserRole
import com.example.karigar.ui.customer.ConfirmLocationScreen
import com.example.karigar.ui.customer.CustomerDashboardScreen
import com.example.karigar.ui.customer.DescribeIssueScreen
import com.example.karigar.ui.customer.PostRequestViewModel
import com.example.karigar.ui.customer.ReviewConfirmScreen
import com.example.karigar.ui.customer.SelectServiceScreen
import com.example.karigar.ui.customer.SetPriceScreen
import com.example.karigar.ui.onboarding.OnboardingScreenFirst

@Composable
fun KarigarNavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "onboarding",
        // Optional: Smooth Animations for the whole app
        enterTransition = { slideInHorizontally(initialOffsetX = { 1000 }, animationSpec = tween(400)) },
        exitTransition = { slideOutHorizontally(targetOffsetX = { -1000 }, animationSpec = tween(400)) },
        popEnterTransition = { slideInHorizontally(initialOffsetX = { -1000 }, animationSpec = tween(400)) },
        popExitTransition = { slideOutHorizontally(targetOffsetX = { 1000 }, animationSpec = tween(400)) }
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
                onLoginClick = { navController.navigate("CustomerDashboard") },
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
                    // Navigate to Dashboard on success
                    navController.navigate("CustomerDashboard") {
                        popUpTo("loginSignup") { inclusive = true }
                    }
                }
            )
        }

        // 1. Dashboard Route
        composable("CustomerDashboard") {
            CustomerDashboardScreen(
                onPostRequestClick = {
                    // Navigate to the START of the wizard flow
                    navController.navigate("PostRequestFlow")
                }
            )
        }

        // 2. Post Request Wizard Flow (Nested Graph)
        navigation(startDestination = "SelectService", route = "PostRequestFlow") {

            // Helper to share ViewModel across all screens in this flow
            @Composable
            fun getSharedViewModel(navController: androidx.navigation.NavController): PostRequestViewModel {
                val navBackStackEntry = remember(navController.currentBackStackEntry) {
                    navController.getBackStackEntry("PostRequestFlow")
                }
                return viewModel(navBackStackEntry)
            }

            // Step 1
            composable("SelectService") {
                val vm = getSharedViewModel(navController)
                SelectServiceScreen(
                    viewModel = vm,
                    onBackClick = { navController.popBackStack("CustomerDashboard", inclusive = false) },
                    onNextClick = { navController.navigate("DescribeIssue") }
                )
            }

            // Step 2
            composable("DescribeIssue") {
                val vm = getSharedViewModel(navController)
                DescribeIssueScreen(
                    viewModel = vm,
                    onBackClick = { navController.popBackStack() },
                    onNextClick = { navController.navigate("SetPrice") }
                )
            }

            // Step 3
            composable("SetPrice") {
                val vm = getSharedViewModel(navController)
                SetPriceScreen(
                    viewModel = vm,
                    onBackClick = { navController.popBackStack() },
                    onNextClick = { navController.navigate("ConfirmLocation") }
                )
            }

            // Step 4
            composable("ConfirmLocation") {
                val vm = getSharedViewModel(navController)
                ConfirmLocationScreen(
                    viewModel = vm,
                    onBackClick = { navController.popBackStack() },
                    onNextClick = { navController.navigate("ReviewConfirm") }
                )
            }

            // Step 5
            composable("ReviewConfirm") {
                val vm = getSharedViewModel(navController)
                ReviewConfirmScreen(
                    viewModel = vm,
                    onBackClick = { navController.popBackStack() },
                    onSubmitClick = {
                        // Submit Logic here...
                        println("Request Submitted!")

                        // Navigate back to Dashboard and clear the wizard history
                        navController.navigate("CustomerDashboard") {
                            popUpTo("CustomerDashboard") { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}