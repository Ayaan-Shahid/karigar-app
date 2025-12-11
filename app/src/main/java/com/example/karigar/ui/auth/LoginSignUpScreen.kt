package com.example.karigar.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Handyman
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.karigar.R


@Composable
fun LoginSignupScreen(
    onLoginClick: () -> Unit = {},
    onSignupClick: () -> Unit = {}
) {
    val primary = Color(0xFF28A745)
    val darkGrey = Color(0xFF333333)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(top = 65.dp, bottom = 45.dp, start = 24.dp, end = 24.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // Logo Icon
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .background(
                        color = Color(0x334CAF50), // primary with 20% opacity (#4CAF50/20)
                        shape = RoundedCornerShape(20.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Handyman,
                    contentDescription = "Handyman Icon",
                    tint = primary,
                    modifier = Modifier.size(45.dp)
                )
            }

            Text(
                text = "Karigar",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = darkGrey,
                modifier = Modifier.padding(top = 12.dp)
            )

            Text(
                text = "Your expert technician, just a tap away.",
                fontSize = 16.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 6.dp)
            )
        }

        // Center Image
        Box(
            modifier = Modifier
                .size(300.dp)
                .clip(RoundedCornerShape(24.dp))
        ) {
            AsyncImage(
                model = R.drawable.loginsignupscreen,
                contentDescription = "cute kawaii mechanic with tools",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            // Login Button
            Button(
                onClick = onLoginClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = primary,
                    contentColor = Color.White
                )
            ) {
                Text("Login", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Sign Up Button
            OutlinedButton(
                onClick = onSignupClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = CircleShape,
                colors = ButtonDefaults.outlinedButtonColors(contentColor = darkGrey)
            ) {
                Text("Sign Up", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }

            Text(
                text = "By continuing, you agree to our Terms of Service and Privacy Policy.",
                fontSize = 11.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginSignupScreenPreview() {
    LoginSignupScreen()
}

