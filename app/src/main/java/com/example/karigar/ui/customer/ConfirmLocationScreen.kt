package com.example.karigar.ui.customer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.karigar.ui.components.PostRequestLayout

// 1. STATEFUL Composable
@Composable
fun ConfirmLocationScreen(
    viewModel: PostRequestViewModel,
    onBackClick: () -> Unit,
    onNextClick: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    ConfirmLocationContent(
        address = state.address,
        houseNo = state.houseNo, // Pass the current value
        onHouseNoChange = { viewModel.updateHouseNo(it) }, // Update ViewModel on typing
        onAddressChangeClick = { /* TODO: Google Maps Integration later */ },
        onBackClick = onBackClick,
        onNextClick = onNextClick
    )
}

// 2. STATELESS Composable
@Composable
fun ConfirmLocationContent(
    address: String,
    houseNo: String,
    onHouseNoChange: (String) -> Unit,
    onAddressChangeClick: () -> Unit,
    onBackClick: () -> Unit,
    onNextClick: () -> Unit
) {
    val primaryColor = Color(0xFF4CAF50)

    PostRequestLayout(
        title = "Confirm Location",
        currentStep = 4,
        onBackClick = onBackClick,
        bottomBar = {
            Button(
                onClick = onNextClick,
                modifier = Modifier.fillMaxWidth().padding(16.dp).height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
                shape = RoundedCornerShape(12.dp)
            ) { Text("Confirm & Continue", fontSize = 16.sp, fontWeight = FontWeight.Bold) }
        }
    ) {
        // Map Placeholder
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .background(Color.LightGray, RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Filled.LocationOn, null, tint = Color.Red, modifier = Modifier.size(48.dp))
            Text("Map View Placeholder", color = Color.Black, modifier = Modifier.padding(top = 60.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Address Card
        Card(
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(color = primaryColor.copy(0.1f), shape = RoundedCornerShape(8.dp), modifier = Modifier.size(48.dp)) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(Icons.Filled.LocationOn, null, tint = primaryColor)
                    }
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(address, fontWeight = FontWeight.Bold)
                    Text("Is this correct?", fontSize = 12.sp, color = Color.Gray)
                }
                TextButton(onClick = onAddressChangeClick) {
                    Text("Change", color = primaryColor)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // House No Input (FIXED)
        Text("Apartment/House No (Optional)", fontWeight = FontWeight.Medium, modifier = Modifier.padding(bottom = 8.dp))
        OutlinedTextField(
            value = houseNo, // Now bound to state
            onValueChange = onHouseNoChange, // Now updates state
            placeholder = { Text("e.g. House 123") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = primaryColor,
                unfocusedBorderColor = Color.LightGray
            ),
            singleLine = true
        )
    }
}

// 3. PREVIEW
@Preview(showBackground = true)
@Composable
fun ConfirmLocationPreview() {
    ConfirmLocationContent(
        address = "F-8 Markaz, Islamabad",
        houseNo = "",
        onHouseNoChange = {},
        onAddressChangeClick = {},
        onBackClick = {},
        onNextClick = {}
    )
}