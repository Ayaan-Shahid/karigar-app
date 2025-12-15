package com.example.karigar.ui.customer

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ListAlt
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Construction
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Plumbing
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.karigar.ui.components.AiChatBottomSheet // Ensure this import exists
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnrememberedMutableState")
@Composable
fun CustomerDashboardScreen(
    onPostRequestClick: () -> Unit
) {
    val primaryColor = Color(0xFF4CAF50)
    val cardLight = Color(0xFFFFFFFF)
    val textLight = Color(0xFF333333)

    // Islamabad Coordinates for the demo
    val islamabad = LatLng(33.6844, 73.0479)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(islamabad, 12f)
    }

    // --- AI Chat State ---
    var showChatSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)

    Scaffold(
        bottomBar = {
            CustomerBottomNavigation()
        },
        // We use a Box to layer the UI on top of the Map
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues) // Respect bottom bar height
            ) {
                // 1. THE MAP BACKGROUND
                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = cameraPositionState
                ) {
                    // Technician Pins
                    Marker(
                        state = MarkerState(position = LatLng(33.69, 73.05)),
                        title = "Electrician",
                        icon = null
                    )
                    Marker(
                        state = MarkerState(position = LatLng(33.67, 73.03)),
                        title = "Plumber"
                    )
                    Marker(
                        state = MarkerState(position = LatLng(33.70, 73.06)),
                        title = "Construction"
                    )
                }

                // 2. MAIN UI OVERLAY (Gradient Top + Search + Chips)
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color.White.copy(alpha = 0.9f),
                                    Color.White.copy(alpha = 0.7f),
                                    Color.Transparent
                                )
                            )
                        )
                        .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 24.dp)
                ) {
                    // Top Bar
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Hello, User!",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = textLight
                        )
                        Surface(
                            shape = CircleShape,
                            color = cardLight,
                            shadowElevation = 4.dp,
                            modifier = Modifier.size(40.dp)
                        ) {
                            IconButton(onClick = { /* TODO */ }) {
                                Icon(
                                    imageVector = Icons.Default.Notifications,
                                    contentDescription = "Notifications",
                                    tint = textLight
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Search Bar
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = cardLight,
                        shadowElevation = 4.dp,
                        modifier = Modifier.fillMaxWidth().height(56.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Spacer(modifier = Modifier.width(16.dp))
                            Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray)
                            TextField(
                                value = "",
                                onValueChange = {},
                                placeholder = { Text("Search for a service...") },
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedContainerColor = Color.Transparent,
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent
                                ),
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Chips/Filters
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        item { FilterChip(text = "All", icon = Icons.Default.Tune, selected = true) }
                        item { FilterChip(text = "Electrician", icon = Icons.Default.Bolt, selected = false) }
                        item { FilterChip(text = "Plumber", icon = Icons.Default.Plumbing, selected = false) }
                        item { FilterChip(text = "Urgent", icon = Icons.Default.Construction, selected = false) }
                    }
                }

                // 3. FLOATING BUTTONS (Post Request & AI Chat)

                // Post Request FAB (Bottom Right)
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(bottom = 24.dp, end = 16.dp)
                ) {
                    Button(
                        onClick = onPostRequestClick,
                        colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
                        shape = RoundedCornerShape(50),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp),
                        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, tint = Color.White)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Post Request", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }

                // AI Chatbot Icon (Bottom Left)
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(bottom = 24.dp, start = 16.dp)
                ) {
                    Surface(
                        shape = CircleShape,
                        color = cardLight,
                        shadowElevation = 6.dp,
                        modifier = Modifier
                            .size(56.dp)
                            .clickable { showChatSheet = true } // CLICK ACTION ADDED
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.SmartToy,
                                contentDescription = "AI Chat",
                                tint = primaryColor,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }
                }

                // 4. AI CHAT BOTTOM SHEET
                if (showChatSheet) {
                    AiChatBottomSheet(
                        onDismiss = { showChatSheet = false },
                        sheetState = sheetState
                    )
                }
            }
        }
    )
}

@Composable
fun FilterChip(text: String, icon: ImageVector, selected: Boolean) {
    val primaryColor = Color(0xFF4CAF50)
    val backgroundColor = if (selected) primaryColor else Color.White
    val contentColor = if (selected) Color.White else Color(0xFF333333)

    Surface(
        color = backgroundColor,
        shape = RoundedCornerShape(50),
        shadowElevation = 2.dp,
        modifier = Modifier.height(40.dp).clickable { }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = text, color = contentColor, fontSize = 14.sp, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
fun CustomerBottomNavigation() {
    var selectedItem by remember { mutableStateOf(0) }

    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home") },
            selected = selectedItem == 0,
            onClick = { selectedItem = 0 },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF4CAF50),
                selectedTextColor = Color(0xFF4CAF50),
                indicatorColor = Color(0xFFE8F5E9)
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.AutoMirrored.Filled.ListAlt, contentDescription = "Requests") },
            label = { Text("My Requests") },
            selected = selectedItem == 1,
            onClick = { selectedItem = 1 },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF4CAF50),
                selectedTextColor = Color(0xFF4CAF50),
                indicatorColor = Color(0xFFE8F5E9)
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
            label = { Text("Profile") },
            selected = selectedItem == 2,
            onClick = { selectedItem = 2 },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF4CAF50),
                selectedTextColor = Color(0xFF4CAF50),
                indicatorColor = Color(0xFFE8F5E9)
            )
        )
    }
}

@Preview
@Composable
fun CustomerDashboardPreview() {
    CustomerDashboardScreen(
        onPostRequestClick = {}
    )
}