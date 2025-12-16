package com.example.karigar.ui.customer

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton // Import this
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.karigar.ui.components.PostRequestLayout
import com.example.karigar.ui.viewmodel.PostRequestViewModel
import com.example.karigar.ui.viewmodel.SubmissionState

// 1. STATEFUL Composable
@Composable
fun ReviewConfirmScreen(
    viewModel: PostRequestViewModel,
    onBackClick: () -> Unit,
    onSubmitClick: () -> Unit,
    onEditService: () -> Unit,
    onEditLocation: () -> Unit,
    onEditPrice: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val submissionStatus by viewModel.submissionStatus.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(submissionStatus) {
        when (submissionStatus) {
            is SubmissionState.Success -> {
                viewModel.resetSubmissionStatus()
                onSubmitClick()
            }
            is SubmissionState.Error -> {
                val errorMsg = (submissionStatus as SubmissionState.Error).message
                Toast.makeText(context, "Error: $errorMsg", Toast.LENGTH_LONG).show()
                viewModel.resetSubmissionStatus()
            }
            else -> {}
        }
    }

    ReviewConfirmContent(
        serviceCategory = state.serviceCategory,
        description = state.description,
        imageUri = state.imageUri,
        address = "${state.address}, ${state.houseNo}",
        scheduledDate = state.scheduledDate,
        price = state.price,
        isLoading = submissionStatus is SubmissionState.Loading,
        onBackClick = onBackClick,
        onSubmitClick = { viewModel.submitJob(context) },
        onEditService = onEditService,
        onEditLocation = onEditLocation,
        onEditPrice = onEditPrice
    )
}

// 2. STATELESS Composable
@Composable
fun ReviewConfirmContent(
    serviceCategory: String,
    description: String,
    imageUri: String?,
    address: String,
    scheduledDate: String,
    price: String,
    isLoading: Boolean,
    onBackClick: () -> Unit,
    onSubmitClick: () -> Unit,
    onEditService: () -> Unit,
    onEditLocation: () -> Unit,
    onEditPrice: () -> Unit
) {
    val primaryColor = Color(0xFF4CAF50)

    PostRequestLayout(
        title = "Review & Confirm",
        currentStep = 5,
        totalSteps = 5,
        onBackClick = onBackClick,
        bottomBar = {
            Button(
                onClick = onSubmitClick,
                enabled = !isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
                shape = RoundedCornerShape(12.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Submit Request", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    ) {
        Column(Modifier.verticalScroll(rememberScrollState())) {

            // --- SECTION 1: Service Details ---
            ReviewSection(
                title = "Service Details",
                onEditClick = onEditService
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.Top
                    ) {
                        ReviewIcon(Icons.Filled.AcUnit, primaryColor)
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(serviceCategory.ifEmpty { "Service" }, fontWeight = FontWeight.Bold)
                            Text(
                                description.ifEmpty { "No Description" },
                                fontSize = 14.sp,
                                color = Color.Gray,
                                maxLines = 3,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                    if (imageUri != null) {
                        Spacer(modifier = Modifier.width(8.dp))
                        AsyncImage(
                            model = imageUri,
                            contentDescription = "Issue Image",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(60.dp)
                                .clip(RoundedCornerShape(8.dp))
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // --- SECTION 2: Location ---
            ReviewSection(
                title = "Location",
                onEditClick = onEditLocation
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    ReviewIcon(Icons.Filled.LocationOn, primaryColor)
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text("Address", fontWeight = FontWeight.Bold)
                        Text(address, fontSize = 14.sp, color = Color.Gray)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // --- SECTION 3: Budget ---
            ReviewSection(
                title = "Your Budget",
                onEditClick = onEditPrice
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    ReviewIcon(Icons.Filled.Payments, primaryColor)
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text("Offered Price", fontWeight = FontWeight.Bold)
                        Text("PKR ${price.ifEmpty { "0" }}", fontWeight = FontWeight.Bold, color = primaryColor)
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

// 🟢 FIXED: Replaced Clickable Text with TextButton for better touch response
@Composable
fun ReviewSection(
    title: String,
    onEditClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically // Align edit button center
            ) {
                Text(title, fontWeight = FontWeight.Bold, fontSize = 16.sp)

                // Using TextButton ensures a large clickable area (48dp min)
                TextButton(onClick = onEditClick) {
                    Text(
                        text = "Edit",
                        color = Color(0xFF4CAF50),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
            Spacer(modifier = Modifier.height(4.dp)) // Reduced spacer as TextButton has padding
            content()
        }
    }
}

@Composable
fun ReviewIcon(icon: ImageVector, color: Color) {
    Surface(color = color.copy(0.1f), shape = RoundedCornerShape(8.dp), modifier = Modifier.size(40.dp)) {
        Box(contentAlignment = Alignment.Center) {
            Icon(icon, null, tint = color, modifier = Modifier.size(20.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ReviewConfirmPreview() {
    ReviewConfirmContent(
        serviceCategory = "AC Repair",
        description = "AC is not cooling properly.",
        imageUri = null,
        address = "House 123, Street 4, Islamabad",
        scheduledDate = "Tomorrow, 2:00 PM",
        price = "1500",
        isLoading = false,
        onBackClick = {},
        onSubmitClick = {},
        onEditService = {},
        onEditLocation = {},
        onEditPrice = {}
    )
}