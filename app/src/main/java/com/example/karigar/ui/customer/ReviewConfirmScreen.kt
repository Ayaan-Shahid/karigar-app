package com.example.karigar.ui.customer

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
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.karigar.ui.components.PostRequestLayout
import com.example.karigar.ui.viewmodel.PostRequestViewModel

// 1. STATEFUL Composable (Used in NavGraph)
@Composable
fun ReviewConfirmScreen(
    viewModel: PostRequestViewModel,
    onBackClick: () -> Unit,
    onSubmitClick: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    ReviewConfirmContent(
        serviceCategory = state.serviceCategory,
        description = state.description,
        address = state.address,
        scheduledDate = state.scheduledDate,
        price = state.price,
        onBackClick = onBackClick,
        onSubmitClick = onSubmitClick
    )
}

// 2. STATELESS Composable (Used in Preview)
@Composable
fun ReviewConfirmContent(
    serviceCategory: String,
    description: String,
    address: String,
    scheduledDate: String,
    price: String,
    onBackClick: () -> Unit,
    onSubmitClick: () -> Unit
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
                modifier = Modifier.fillMaxWidth().padding(16.dp).height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
                shape = RoundedCornerShape(12.dp)
            ) { Text("Submit Request", fontSize = 16.sp, fontWeight = FontWeight.Bold) }
        }
    ) {
        Column(Modifier.verticalScroll(rememberScrollState())) {
            ReviewSection(title = "Service Details") {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    ReviewIcon(Icons.Filled.AcUnit, primaryColor)
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(serviceCategory.ifEmpty { "Service" }, fontWeight = FontWeight.Bold)
                        Text(description.ifEmpty { "No Description" }, fontSize = 14.sp, color = Color.Gray)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            ReviewSection(title = "Location & Time") {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    ReviewIcon(Icons.Filled.LocationOn, primaryColor)
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text("Address", fontWeight = FontWeight.Bold)
                        Text(address, fontSize = 14.sp, color = Color.Gray)
                    }
                }
                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color(0xFFF0F0F0))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    ReviewIcon(Icons.Filled.CalendarMonth, primaryColor)
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text("Scheduled For", fontWeight = FontWeight.Bold)
                        Text(scheduledDate, fontSize = 14.sp, color = Color.Gray)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            ReviewSection(title = "Your Budget") {
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

@Composable
fun ReviewSection(title: String, content: @Composable () -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text("Edit", color = Color(0xFF4CAF50), fontSize = 14.sp, fontWeight = FontWeight.Medium)
            }
            Spacer(modifier = Modifier.height(12.dp))
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

// 3. PREVIEW
@Preview(showBackground = true)
@Composable
fun ReviewConfirmPreview() {
    ReviewConfirmContent(
        serviceCategory = "AC Repair",
        description = "AC is not cooling properly.",
        address = "House 123, Street 4, Islamabad",
        scheduledDate = "Tomorrow, 2:00 PM",
        price = "1500",
        onBackClick = {},
        onSubmitClick = {}
    )
}