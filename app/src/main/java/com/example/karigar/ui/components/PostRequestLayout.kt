package com.example.karigar.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding // <--- IMPORT THIS
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PostRequestLayout(
    title: String,
    currentStep: Int,
    totalSteps: Int = 5,
    onBackClick: () -> Unit,
    bottomBar: @Composable () -> Unit = {},
    content: @Composable ColumnScope.() -> Unit
) {
    val backgroundLight = Color(0xFFF7F9F9)
    val textDark = Color(0xFF102210)

    Scaffold(
        containerColor = backgroundLight,
        topBar = {
            Column(modifier = Modifier
                .background(backgroundLight)
                .statusBarsPadding()
            ) {
                // Top Bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBackClick, modifier = Modifier.size(40.dp)) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = textDark)
                    }
                    Text(
                        text = title,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = textDark,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.size(40.dp)) // Balance
                }

                // (Progress indicators commented out in your code)
            }
        },
        // --- BOTTOM BAR FIX ---
        bottomBar = {
            Box(
                modifier = Modifier
                    .background(backgroundLight) // Match background color
                    .navigationBarsPadding()     // Pushes content up above system nav bar
            ) {
                bottomBar()
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            content()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PostRequestLayoutPreview() {
    PostRequestLayout(
        title = "Select a Service",
        currentStep = 2,
        totalSteps = 5,
        onBackClick = {},
        content = {
            androidx.compose.material3.Text(
                text = "This is where the screen content goes.",
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    )
}