package com.example.karigar.ui.customer

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Data model for the grid items
data class ServiceCategory(val name: String, val icon: ImageVector)

@Composable
fun SelectServiceScreen(
    onBackClick: () -> Unit,
    onNextClick: (String) -> Unit // Pass selected category name
) {
    // Colors from your theme
    val primaryColor = Color(0xFF4CAF50)
    val backgroundLight = Color(0xFFF7F9F9)
    val textDark = Color(0xFF102210)
    val cardBorder = Color(0xFFE5E7EB)

    // State
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<String?>(null) }

    // Service List (Matching the HTML design)
    val services = listOf(
        ServiceCategory("Electrician", Icons.Filled.Bolt), // Generic for electrical
        ServiceCategory("Plumber", Icons.Filled.Plumbing),
        ServiceCategory("AC Technician", Icons.Filled.AcUnit),
        ServiceCategory(
            "Carpenter",
            Icons.Filled.Carpenter
        ), // Requires extended icons or use Build
        ServiceCategory("Painter", Icons.Filled.FormatPaint),
        ServiceCategory("Appliance Repair", Icons.Filled.Microwave),
        ServiceCategory("Cleaning", Icons.Filled.CleaningServices),
        ServiceCategory("Other", Icons.Filled.MoreHoriz)
    )

    Scaffold(
        containerColor = backgroundLight,
        topBar = {
            // Top App Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = textDark
                    )
                }
                Text(
                    text = "Select a Service",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = textDark,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
                // Empty box to balance the title centering
                Spacer(modifier = Modifier.size(48.dp))
            }
        },
        bottomBar = {
            // Sticky Bottom Button
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(backgroundLight)
                    .padding(16.dp)
            ) {
                Button(
                    onClick = { selectedCategory?.let { onNextClick(it) } },
                    enabled = selectedCategory != null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = primaryColor,
                        contentColor = Color.White,
                        disabledContainerColor = Color.Gray.copy(alpha = 0.5f)
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Next", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            // Page Indicators (Step 1 of 5)
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Step 1 of 5: Category",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Active Step
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(8.dp)
                            .clip(CircleShape)
                            .background(primaryColor)
                    )
                    // Inactive Steps
                    repeat(4) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(8.dp)
                                .clip(CircleShape)
                                .background(Color.LightGray)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Search Bar
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color.White,
                border = androidx.compose.foundation.BorderStroke(1.dp, cardBorder), // Light border
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 12.dp)
                ) {
                    Icon(Icons.Filled.Search, contentDescription = null, tint = Color.Gray)
                    Spacer(modifier = Modifier.width(8.dp))
                    BasicTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        textStyle = androidx.compose.ui.text.TextStyle(
                            fontSize = 16.sp,
                            color = textDark
                        ),
                        singleLine = true,
                        decorationBox = { innerTextField ->
                            if (searchQuery.isEmpty()) {
                                Text(
                                    "Search for a service e.g. AC Repair",
                                    color = Color.Gray,
                                    fontSize = 16.sp
                                )
                            }
                            innerTextField()
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Grid Categories
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 140.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(services.filter {
                    it.name.contains(
                        searchQuery,
                        ignoreCase = true
                    )
                }) { service ->
                    ServiceCard(
                        service = service,
                        isSelected = selectedCategory == service.name,
                        primaryColor = primaryColor,
                        onClick = { selectedCategory = service.name }
                    )
                }
            }
        }
    }
}

@Composable
fun ServiceCard(
    service: ServiceCategory,
    isSelected: Boolean,
    primaryColor: Color,
    onClick: () -> Unit
) {
    val backgroundColor = if (isSelected) primaryColor.copy(alpha = 0.1f) else Color.White
    val borderColor = if (isSelected) primaryColor else Color(0xFFE5E7EB)
    val iconColor = if (isSelected) primaryColor else Color(0xFF333333)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f) // Makes it square
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        border = androidx.compose.foundation.BorderStroke(
            if (isSelected) 2.dp else 1.dp,
            borderColor
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = service.icon,
                contentDescription = service.name,
                tint = iconColor,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = service.name,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF333333),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SelectServicePreview() {
    SelectServiceScreen(onBackClick = {}, onNextClick = {})
}