package com.example.karigar.ui.customer

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.karigar.ui.components.PostRequestLayout

// 1. STATEFUL Composable (Used in NavGraph)
@Composable
fun SetPriceScreen(
    viewModel: PostRequestViewModel,
    onBackClick: () -> Unit,
    onNextClick: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    SetPriceContent(
        initialPrice = state.price,
        onPriceUpdated = { viewModel.updatePrice(it) },
        onBackClick = onBackClick,
        onNextClick = onNextClick
    )
}

// 2. STATELESS Composable (Used in Preview)
@Composable
fun SetPriceContent(
    initialPrice: String,
    onPriceUpdated: (String) -> Unit,
    onBackClick: () -> Unit,
    onNextClick: () -> Unit
) {
    // Local state for immediate typing feedback
    var priceInput by remember { mutableStateOf(initialPrice) }
    val primaryColor = Color(0xFF4CAF50)

    PostRequestLayout(
        title = "Set Your Price",
        currentStep = 3,
        onBackClick = onBackClick,
        bottomBar = {
            Button(
                onClick = {
                    onPriceUpdated(priceInput)
                    onNextClick()
                },
                enabled = priceInput.isNotEmpty(),
                modifier = Modifier.fillMaxWidth().padding(16.dp).height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
                shape = RoundedCornerShape(12.dp)
            ) { Text("Review Request", fontSize = 16.sp, fontWeight = FontWeight.Bold) }
        }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth().padding(top = 24.dp)
        ) {
            Text("What's your offer?", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            Text("Enter amount you're willing to pay", fontSize = 16.sp, color = Color.Gray, modifier = Modifier.padding(top = 8.dp, bottom = 32.dp))

            // Large Price Input
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .background(Color.White, RoundedCornerShape(16.dp))
                    .border(1.dp, Color.LightGray, RoundedCornerShape(16.dp))
            ) {
                Text("PKR", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Color.Gray, modifier = Modifier.padding(end = 16.dp))
                BasicTextField(
                    value = priceInput,
                    onValueChange = { if (it.all { char -> char.isDigit() }) priceInput = it },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    textStyle = TextStyle(fontSize = 40.sp, fontWeight = FontWeight.Bold, color = Color.Black),
                    singleLine = true,
                    modifier = Modifier.width(IntrinsicSize.Min)
                )
                if(priceInput.isEmpty()) {
                    Text("1000", fontSize = 40.sp, fontWeight = FontWeight.Bold, color = Color.LightGray)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // AI Suggestion
            Card(
                colors = CardDefaults.cardColors(containerColor = primaryColor.copy(alpha = 0.1f)),
                border = androidx.compose.foundation.BorderStroke(1.dp, primaryColor.copy(alpha = 0.3f)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp).fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("AI SUGGESTION", color = primaryColor, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    Text("Market rate is around PKR 1,200 - 1,500", fontWeight = FontWeight.Medium, modifier = Modifier.padding(vertical = 4.dp))
                    Text("This is a starting offer. You can negotiate later.", fontSize = 12.sp, color = Color.Gray, textAlign = TextAlign.Center)
                }
            }
        }
    }
}

// 3. PREVIEW
@Preview(showBackground = true)
@Composable
fun SetPricePreview() {
    SetPriceContent(
        initialPrice = "",
        onPriceUpdated = {},
        onBackClick = {},
        onNextClick = {}
    )
}