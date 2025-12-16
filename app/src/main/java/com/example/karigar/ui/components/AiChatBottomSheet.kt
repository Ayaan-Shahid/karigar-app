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
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.collectAsState
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.karigar.ui.viewmodel.ChatMessage
import com.example.karigar.ui.viewmodel.ChatViewModel

// ---------------------------------------------------------
// 1. The "Smart" Component (Handles Logic & ViewModel)
// ---------------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiChatBottomSheet(
    onDismiss: () -> Unit,
    sheetState: SheetState,
    viewModel: ChatViewModel = viewModel()
) {
    // Collect real data from ViewModel
    val messages by viewModel.messages.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White,
        scrimColor = Color.Black.copy(alpha = 0.5f),
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        dragHandle = { BottomSheetDefaults.DragHandle() }
    ) {
        Box(modifier = Modifier.fillMaxHeight(0.9f)) {
            // Pass state down to the UI
            AiChatContent(
                messages = messages,
                isLoading = isLoading,
                onSendMessage = { viewModel.sendMessage(it) },
                onDismiss = onDismiss
            )
        }
    }
}

// ---------------------------------------------------------
// 2. The "Dumb" UI Component (Stateless - Perfect for Previews)
// ---------------------------------------------------------
@Composable
fun AiChatContent(
    messages: List<ChatMessage>,
    isLoading: Boolean,
    onSendMessage: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val primaryColor = Color(0xFF4CAF50)
    val listState = rememberLazyListState()
    var inputText by remember { mutableStateOf("") }

    // Auto-scroll to bottom
    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

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
                                Icon(
                                    Icons.Filled.SmartToy,
                                    null,
                                    tint = primaryColor,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                "Karigar Assistant",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .background(if (isLoading) Color(0xFFFFC107) else primaryColor, CircleShape)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    if (isLoading) "Typing..." else "Online",
                                    color = if (isLoading) Color(0xFFFFC107) else primaryColor,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
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
            Surface(
                shadowElevation = 12.dp,
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .imePadding()
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
                        modifier = Modifier.weight(1f),
                        enabled = !isLoading
                    )

                    IconButton(
                        onClick = {
                            if (inputText.isNotBlank()) {
                                onSendMessage(inputText)
                                inputText = ""
                            }
                        },
                        enabled = !isLoading && inputText.isNotBlank(),
                        modifier = Modifier
                            .padding(end = 4.dp)
                            .background(
                                if (!isLoading && inputText.isNotBlank()) primaryColor else Color.LightGray,
                                CircleShape
                            )
                            .size(40.dp)
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                        } else {
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
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
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

            if (messages.isEmpty()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    SuggestionChip(
                        onClick = { onSendMessage("I need an electrician") },
                        label = { Text("Electrician") },
                        colors = SuggestionChipDefaults.suggestionChipColors(containerColor = Color(0xFFF5F5F5)),
                        border = null
                    )
                    SuggestionChip(
                        onClick = { onSendMessage("How much for AC repair?") },
                        label = { Text("Check Rates") },
                        colors = SuggestionChipDefaults.suggestionChipColors(containerColor = Color(0xFFF5F5F5)),
                        border = null
                    )
                }
            }
        }
    }
}

// ---------------------------------------------------------
// 3. Helper Components
// ---------------------------------------------------------
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
    }
}

// ---------------------------------------------------------
// 4. The Preview (Uses 'Dumb' Component with Fake Data)
// ---------------------------------------------------------
@Preview(showBackground = true)
@Composable
fun AiChatPreview() {
    val fakeMessages = listOf(
        ChatMessage("Hello! How can I help you today?", isUser = false),
        ChatMessage("I need to fix my AC.", isUser = true),
        ChatMessage("Sure! I can help you find a technician.", isUser = false)
    )

    // We pass the fake messages directly to the UI
    AiChatContent(
        messages = fakeMessages,
        isLoading = false,
        onSendMessage = {}, // Do nothing on send
        onDismiss = {}
    )
}