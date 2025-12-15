package com.example.karigar.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Data class
data class ChatMessage(
    val text: String,
    val isUser: Boolean,
    val timestamp: String = "Just now"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiChatBottomSheet(
    onDismiss: () -> Unit,
    sheetState: SheetState
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White,
        scrimColor = Color.Black.copy(alpha = 0.5f),
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        dragHandle = { BottomSheetDefaults.DragHandle() }
    ) {
        // Force the sheet to be tall (90% of screen height)
        Box(modifier = Modifier.fillMaxHeight(0.9f)) {
            AiChatContent(onDismiss = onDismiss)
        }
    }
}

@Composable
fun AiChatContent(
    onDismiss: () -> Unit
) {
    val primaryColor = Color(0xFF4CAF50)
    val listState = rememberLazyListState()

    var messages by remember { mutableStateOf(listOf(
        ChatMessage("Hi! I'm Karigar AI. How can I help you find a technician today?", false),
    )) }
    var inputText by remember { mutableStateOf("") }

    // Auto-scroll to bottom on new message
    LaunchedEffect(messages.size) {
        listState.animateScrollToItem(messages.size - 1)
    }

    // REAL WORLD FIX: Use Scaffold inside the Sheet.
    // Scaffold allows us to define a TopBar and BottomBar (Input) that are fixed,
    // while the content (LazyColumn) scrolls in the middle.
    Scaffold(
        containerColor = Color.White,
        topBar = {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            color = primaryColor.copy(alpha = 0.1f),
                            shape = CircleShape,
                            modifier = Modifier.size(40.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Filled.SmartToy, null, tint = primaryColor, modifier = Modifier.size(24.dp))
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("Karigar Assistant", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                            Text("Online", color = primaryColor, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                        }
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Filled.Close, "Close", tint = Color.Gray)
                    }
                }
                HorizontalDivider(color = Color(0xFFF0F0F0), thickness = 1.dp)
            }
        },
        bottomBar = {
            // INPUT AREA: Pinned to bottom automatically by Scaffold
            Surface(
                shadowElevation = 12.dp,
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    // KEY FIXES:
                    .navigationBarsPadding() // Pushes up above system nav bar
                    .imePadding()            // Pushes up above keyboard
            ) {
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .background(Color(0xFFF5F5F5), RoundedCornerShape(24.dp))
                        .padding(horizontal = 4.dp, vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextField(
                        value = inputText,
                        onValueChange = { inputText = it },
                        placeholder = { Text("Ask something...", color = Color.Gray) },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            cursorColor = primaryColor
                        ),
                        modifier = Modifier.weight(1f)
                    )

                    IconButton(
                        onClick = {
                            if (inputText.isNotBlank()) {
                                messages = messages + ChatMessage(inputText, true)
                                inputText = ""
                            }
                        },
                        modifier = Modifier
                            .padding(end = 4.dp)
                            .background(primaryColor, CircleShape)
                            .size(40.dp)
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.Send,
                            "Send",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        // CONTENT AREA: Respects top/bottom bars automatically via innerPadding
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            // Chat List
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(messages) { msg ->
                    ChatBubble(message = msg, primaryColor = primaryColor)
                }
            }

            // Suggestions (Only visible when chat is empty/new)
            if (messages.size == 1) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    SuggestionChip(
                        onClick = { messages = messages + ChatMessage("Find an Electrician", true) },
                        label = { Text("Find Electrician") },
                        colors = SuggestionChipDefaults.suggestionChipColors(containerColor = Color(0xFFF5F5F5)),
                        border = null
                    )
                    SuggestionChip(
                        onClick = { messages = messages + ChatMessage("Check Rates", true) },
                        label = { Text("Check Rates") },
                        colors = SuggestionChipDefaults.suggestionChipColors(containerColor = Color(0xFFF5F5F5)),
                        border = null
                    )
                }
            }
        }
    }
}

@Composable
fun ChatBubble(message: ChatMessage, primaryColor: Color) {
    val align = if (message.isUser) Alignment.End else Alignment.Start
    val bg = if (message.isUser) primaryColor else Color(0xFFF5F5F5)
    val textColor = if (message.isUser) Color.White else Color.Black
    val shape = if (message.isUser) {
        RoundedCornerShape(topStart = 16.dp, topEnd = 4.dp, bottomStart = 16.dp, bottomEnd = 16.dp)
    } else {
        RoundedCornerShape(topStart = 4.dp, topEnd = 16.dp, bottomStart = 16.dp, bottomEnd = 16.dp)
    }

    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = align) {
        Box(
            modifier = Modifier
                .widthIn(max = 280.dp)
                .background(bg, shape)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Text(message.text, color = textColor, fontSize = 15.sp, lineHeight = 22.sp)
        }
        Text(
            message.timestamp,
            fontSize = 11.sp,
            color = Color.Gray,
            modifier = Modifier.padding(top = 4.dp, start = 4.dp, end = 4.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AiChatPreview() {
    AiChatContent(onDismiss = {})
}