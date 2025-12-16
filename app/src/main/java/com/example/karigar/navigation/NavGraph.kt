package com.example.karigar.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import com.example.karigar.ui.customer.ReviewConfirmScreen
import com.example.karigar.ui.customer.SelectServiceScreen
import com.example.karigar.ui.customer.SetPriceScreen
import com.example.karigar.ui.onboarding.OnboardingScreenFirst
import com.example.karigar.ui.viewmodel.PostRequestViewModel

@Composable
fun KarigarNavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "onboarding",
        enterTransition = {
            slideInHorizontally(initialOffsetX = { 1000 }, animationSpec = tween(400))
        },
        exitTransition = {
            slideOutHorizontally(targetOffsetX = { -1000 }, animationSpec = tween(400))
        },
        popEnterTransition = {
            slideInHorizontally(initialOffsetX = { -1000 }, animationSpec = tween(400))
        },
        popExitTransition = {
            slideOutHorizontally(targetOffsetX = { 1000 }, animationSpec = tween(400))
        }
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
                    val role = try { UserRole.valueOf(roleName) } catch (e: Exception) { UserRole.CUSTOMER }
                    println("Signup Success: $role with $phone")
                    navController.navigate("CustomerDashboard") { popUpTo("loginSignup") { inclusive = true } }
                }
            )
        }

        composable("CustomerDashboard") {
            CustomerDashboardScreen(
                onPostRequestClick = { navController.navigate("PostRequestFlow") }
            )
        }

        navigation(startDestination = "SelectService", route = "PostRequestFlow") {

            @Composable
            fun getSharedViewModel(entry: androidx.navigation.NavBackStackEntry): PostRequestViewModel {
                val parentEntry = remember(entry) { navController.getBackStackEntry("PostRequestFlow") }
                return viewModel(parentEntry)
            }

            // Step 1: Select Service
            composable("SelectService") { entry ->
                val vm = getSharedViewModel(entry)
                SelectServiceScreen(
                    viewModel = vm,
                    onBackClick = { navController.popBackStack() },
                    onNextClick = {
                        // Always go to DescribeIssue (Linear flow for editing 1 & 2)
                        navController.navigate("DescribeIssue")
                    }
                )
            }

            // Step 2: Describe Issue
            composable("DescribeIssue") { entry ->
                val vm = getSharedViewModel(entry)
                val state = vm.uiState.collectAsState().value
                DescribeIssueScreen(
                    viewModel = vm,
                    onBackClick = { navController.popBackStack() },
                    onNextClick = {
                        // If Editing, done -> Review. If new flow -> SetPrice
                        if (state.isEditMode) {
                            navController.navigate("ReviewConfirm")
                        } else {
                            navController.navigate("SetPrice")
                        }
                    }
                )
            }

            // Step 3: Set Price
            composable("SetPrice") { entry ->
                val vm = getSharedViewModel(entry)
                val state = vm.uiState.collectAsState().value
                SetPriceScreen(
                    viewModel = vm,
                    onBackClick = { navController.popBackStack() },
                    onNextClick = {
                        // If Editing, done -> Review. If new flow -> Location
                        if (state.isEditMode) {
                            navController.navigate("ReviewConfirm")
                        } else {
                            navController.navigate("ConfirmLocation")
                        }
                    }
                )
            }

            // Step 4: Confirm Location
            composable("ConfirmLocation") { entry ->
                val vm = getSharedViewModel(entry)
                ConfirmLocationScreen(
                    viewModel = vm,
                    onBackClick = { navController.popBackStack() },
                    onNextClick = {
                        // Always go to Review (Last step)
                        navController.navigate("ReviewConfirm")
                    }
                )
            }

            // Step 5: Review & Confirm
            composable("ReviewConfirm") { entry ->
                val vm = getSharedViewModel(entry)

                ReviewConfirmScreen(
                    viewModel = vm,
                    onBackClick = { navController.popBackStack() },

                    // 🟢 UPDATED: Use 'navigate' instead of 'popBackStack' for robustness

                    // Edit Service: Go to Step 1 (SelectService)
                    onEditService = {
                        vm.setEditMode(true)
                        navController.navigate("SelectService")
                    },

                    // Edit Location: Go to Step 4 (ConfirmLocation)
                    onEditLocation = {
                        vm.setEditMode(true)
                        navController.navigate("ConfirmLocation")
                    },

                    // Edit Price: Go to Step 3 (SetPrice)
                    onEditPrice = {
                        vm.setEditMode(true)
                        navController.navigate("SetPrice")
                    },

                    onSubmitClick = {
                        println("Request Submitted!")
                        navController.navigate("CustomerDashboard") {
                            popUpTo("CustomerDashboard") { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}