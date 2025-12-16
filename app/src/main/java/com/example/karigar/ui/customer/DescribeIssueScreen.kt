package com.example.karigar.ui.customer

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage // Requires coil-compose dependency
import com.example.karigar.ui.components.PostRequestLayout
import com.example.karigar.ui.viewmodel.PostRequestViewModel

// 1. STATEFUL Composable
@Composable
fun DescribeIssueScreen(
    viewModel: PostRequestViewModel,
    onBackClick: () -> Unit,
    onNextClick: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    DescribeIssueContent(
        initialDescription = state.description,
        initialImageUri = state.imageUri, // Pass existing image if any
        onUpdateDescription = { text, uri -> viewModel.updateIssueDetails(text, uri) },
        onBackClick = onBackClick,
        onNextClick = onNextClick
    )
}

// 2. STATELESS Composable
@Composable
fun DescribeIssueContent(
    initialDescription: String,
    initialImageUri: String?,
    onUpdateDescription: (String, String?) -> Unit,
    onBackClick: () -> Unit,
    onNextClick: () -> Unit
) {
    val primaryColor = Color(0xFF4CAF50)

    // Local state for immediate feedback
    var text by remember { mutableStateOf(initialDescription) }
    var selectedUri by remember { mutableStateOf<Uri?>(initialImageUri?.let { Uri.parse(it) }) }

    // --- PHOTO PICKER LAUNCHER ---
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            if (uri != null) {
                selectedUri = uri
            }
        }
    )

    PostRequestLayout(
        title = "Describe Issue",
        currentStep = 2,
        onBackClick = onBackClick,
        bottomBar = {
            Button(
                onClick = {
                    // Convert URI to String for ViewModel
                    onUpdateDescription(text, selectedUri?.toString())
                    onNextClick()
                },
                enabled = text.isNotEmpty(),
                modifier = Modifier.fillMaxWidth().padding(16.dp).height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
                shape = RoundedCornerShape(12.dp)
            ) { Text("Continue", fontSize = 16.sp, fontWeight = FontWeight.Bold) }
        }
    ) {
        Column(Modifier.verticalScroll(rememberScrollState())) {
            Text("What do you need help with?", fontWeight = FontWeight.Medium, fontSize = 16.sp, modifier = Modifier.padding(bottom = 8.dp))

            Box(
                Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(12.dp))
                    .background(Color.White)
                    .padding(16.dp)
            ) {
                if (text.isEmpty()) Text("Example: AC is making noise...", color = Color.Gray)
                BasicTextField(
                    value = text,
                    onValueChange = { if (it.length <= 500) text = it },
                    textStyle = TextStyle(fontSize = 16.sp, color = Color.Black),
                    modifier = Modifier.fillMaxSize()
                )
            }
            Text("${text.length}/500", color = Color.Gray, fontSize = 12.sp, modifier = Modifier.align(Alignment.End).padding(top = 4.dp))

            Spacer(modifier = Modifier.height(24.dp))

            Text("Add a Photo (Optional)", fontWeight = FontWeight.Medium, fontSize = 16.sp)
            Text("Helps technicians understand better.", color = Color.Gray, fontSize = 14.sp, modifier = Modifier.padding(bottom = 12.dp))

            // --- IMAGE PICKER UI ---
            if (selectedUri == null) {
                // 1. Placeholder State (Add Photo)
                Box(
                    Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White)
                        .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(12.dp))
                        .clickable {
                            // Launch the picker for Images only
                            photoPickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Filled.AddAPhoto, null, tint = Color.Gray)
                }
            } else {
                // 2. Selected State (Show Preview + Remove Button)
                Box(
                    Modifier.size(100.dp)
                ) {
                    AsyncImage(
                        model = selectedUri,
                        contentDescription = "Selected Image",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(12.dp))
                            .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop
                    )

                    // Remove Button (Top Right)
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(4.dp)
                            .size(24.dp)
                            .background(Color.Black.copy(alpha = 0.6f), CircleShape)
                            .clickable { selectedUri = null },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Filled.Close, "Remove", tint = Color.White, modifier = Modifier.size(16.dp))
                    }
                }
            }
        }
    }
}

// 3. PREVIEW
@Preview(showBackground = true)
@Composable
fun DescribeIssuePreview() {
    DescribeIssueContent(
        initialDescription = "",
        initialImageUri = null,
        onUpdateDescription = { _, _ -> },
        onBackClick = {},
        onNextClick = {}
    )
}