package com.example.karigar.ui.customer

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Carpenter
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.FormatPaint
import androidx.compose.material.icons.filled.Microwave
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Plumbing
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.karigar.ui.components.PostRequestLayout

// Data model
data class ServiceCategory(val name: String, val icon: ImageVector)

// ==========================================
// 1. STATEFUL COMPOSABLE (Used in NavGraph)
// ==========================================
@Composable
fun SelectServiceScreen(
    viewModel: PostRequestViewModel,
    onBackClick: () -> Unit,
    onNextClick: () -> Unit
) {
    // Collect state from ViewModel
    val state by viewModel.uiState.collectAsState()

    // Pass state and events to the Stateless composable
    SelectServiceContent(
        selectedCategory = state.serviceCategory,
        onCategorySelected = viewModel::updateCategory,
        onBackClick = onBackClick,
        onNextClick = onNextClick
    )
}

// ==========================================
// 2. STATELESS COMPOSABLE (Used in Preview)
// ==========================================
@Composable
fun SelectServiceContent(
    selectedCategory: String,
    onCategorySelected: (String) -> Unit,
    onBackClick: () -> Unit,
    onNextClick: () -> Unit
) {
    // Local UI state (search query doesn't need to be in ViewModel usually, but can be)
    var searchQuery by remember { mutableStateOf("") }
    val primaryColor = Color(0xFF4CAF50)

    val services = listOf(
        ServiceCategory("Electrician", Icons.Filled.Bolt),
        ServiceCategory("Plumber", Icons.Filled.Plumbing),
        ServiceCategory("AC Technician", Icons.Filled.AcUnit),
        ServiceCategory("Carpenter", Icons.Filled.Carpenter),
        ServiceCategory("Painter", Icons.Filled.FormatPaint),
        ServiceCategory("Appliance Repair", Icons.Filled.Microwave),
        ServiceCategory("Cleaning", Icons.Filled.CleaningServices),
        ServiceCategory("Other", Icons.Filled.MoreHoriz)
    )

    PostRequestLayout(
        title = "Select a Service",
        currentStep = 1,
        onBackClick = onBackClick,
        bottomBar = {
            Button(
                onClick = onNextClick,
                enabled = selectedCategory.isNotEmpty(),
                modifier = Modifier.fillMaxWidth().padding(16.dp).height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
                shape = RoundedCornerShape(12.dp)
            ) { Text("Next", fontSize = 16.sp, fontWeight = FontWeight.Bold) }
        }
    ) {
        // Search Bar
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = Color.White,
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE0E0E0)),
            modifier = Modifier.fillMaxWidth().height(52.dp).padding(vertical = 4.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(horizontal = 12.dp)) {
                Icon(Icons.Filled.Search, null, tint = Color.Gray)
                Spacer(modifier = Modifier.width(8.dp))
                BasicTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    textStyle = androidx.compose.ui.text.TextStyle(fontSize = 16.sp, color = Color.Black),
                    singleLine = true,
                    decorationBox = { inner -> if (searchQuery.isEmpty()) Text("Search e.g. AC Repair", color = Color.Gray) else inner() }
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 140.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(services.filter { it.name.startsWith(searchQuery, true) }) { service ->
                val isSelected = selectedCategory == service.name
                Card(
                    colors = CardDefaults.cardColors(containerColor = if (isSelected) primaryColor.copy(0.1f) else Color.White),
                    border = androidx.compose.foundation.BorderStroke(if (isSelected) 2.dp else 1.dp, if (isSelected) primaryColor else Color(0xFFE0E0E0)),
                    modifier = Modifier.aspectRatio(1f).clickable { onCategorySelected(service.name) }
                ) {
                    Column(
                        Modifier.fillMaxSize().padding(16.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(service.icon, null, tint = if (isSelected) primaryColor else Color.DarkGray, modifier = Modifier.size(40.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(service.name, fontWeight = FontWeight.Bold, color = Color.Black, textAlign = TextAlign.Center)
                    }
                }
            }
        }
    }
}

// ==========================================
// 3. PREVIEW (Uses Stateless Composable)
// ==========================================
@Preview(showBackground = true)
@Composable
fun SelectServicePreview() {
    // We preview the CONTENT, passing dummy data
    SelectServiceContent(
        selectedCategory = "Plumber", // Mock selection
        onCategorySelected = {},
        onBackClick = {},
        onNextClick = {}
    )
}