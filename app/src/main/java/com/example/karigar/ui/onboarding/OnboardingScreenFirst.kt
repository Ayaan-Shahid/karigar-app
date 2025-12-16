package com.example.karigar.ui.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.karigar.R
import kotlinx.coroutines.launch

data class OnboardingItem(
    val imageRes: Int,
    val title: String,
    val description: String
)

@Preview
@Composable
fun OnboardingScreenFirst(
    onContinueToLogin: () -> Unit = {},
    onSkipToLogin: () -> Unit = {}
) {
    val primary = Color(0xFF28A745)
    val backgroundLight = Color(0xFFFCFCFC)
    val backgroundDark = Color(0xFF112114)
    val textPrimary = Color(0xFF333333)

    val isDark = isSystemInDarkTheme()
    val bgColor = if (isDark) backgroundDark else backgroundLight
    val textColor = if (isDark) Color.White else textPrimary

    val onboardingItems = listOf(
        OnboardingItem(
            R.drawable.onboardingscreen_2,
            "Find skilled technicians nearby",
            "Search or post a job for various services and discover professionals in your area."
        ),
        OnboardingItem(
            R.drawable.onboardingscreen_3,
            "Verified Experts You Can Trust",
            "Read genuine customer reviews and view verified profiles before you book."
        ),
        OnboardingItem(
            R.drawable.onboardingscreen_4,
            "Hire & Get Service",
            "Hire nearby technicians instantly at your convenience."
        )
    )

    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { onboardingItems.size }
    )

    val scope = rememberCoroutineScope()

    Scaffold(containerColor = bgColor) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 65.dp, bottom = 30.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                //Top Header Image
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
                        painter = painterResource(R.drawable.app_logo),
                        contentDescription = "App logo",
                        tint = primary,
                        modifier = Modifier.size(70.dp)
                    )
                }
                Text(
                    text =
                        "Karigar",
                    color = textColor,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .fillMaxWidth()
                )
                Text(
                    text =
                        "Find trusted local technicians near you.",
                    color = if (isDark) Color(0xFFCCCCCC) else textPrimary,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .fillMaxWidth()
                )
            }

            // --- PAGER SECTION ---
            Column(
                modifier = Modifier
                    .fillMaxWidth()
//                .weight(1f)
            ) {

                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .fillMaxWidth()
                ) { page ->
                    val item = onboardingItems[page]
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Box(
                            modifier = Modifier
                                .height(218.dp)
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color.LightGray)
                        ) {
                            AsyncImage(
                                model = item.imageRes,
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }

                        Spacer(Modifier.height(20.dp))

                        Text(
                            text = item.title,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = textColor,
                            textAlign = TextAlign.Center
                        )

                        Spacer(Modifier.height(8.dp))

                        Text(
                            text = item.description,
                            fontSize = 16.sp,
                            color = primary,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Spacer(Modifier.height(20.dp))

                PagerDotsIndicator(
                    totalDots = onboardingItems.size,
                    currentIndex = pagerState.currentPage,
                    activeColor = primary,
                    inactiveColor = Color.Gray
                )
            }

            // --- BUTTON SECTION ---
            Column(modifier = Modifier.padding(16.dp)) {

                Button(
                    onClick = {
                        if (pagerState.currentPage < onboardingItems.lastIndex) {
                            scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) }
                        } else {
                            onContinueToLogin()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(
                        containerColor =
                            if (pagerState.currentPage == onboardingItems.lastIndex)
                                Color(0xFFA8D8B9)       // Light green (Get Started)
                            else
                                primary,
                        contentColor =
                            if (pagerState.currentPage == onboardingItems.lastIndex)
                                Color(0xFF333333)       // Dark text for Get Started
                            else
                                Color.White
                    )
                ) {
                    val buttonText =
                        if (pagerState.currentPage == onboardingItems.lastIndex)
                            "Get Started"
                        else
                            "Continue"
                    Text(buttonText, fontSize = 16.sp)
                }

                Spacer(Modifier.height(12.dp))

                OutlinedButton(
                    onClick = onSkipToLogin,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = CircleShape
                ) {
                    Text("Skip", fontSize = 16.sp, color = textColor)
                }
            }
        }
    }
}

@Composable
fun PagerDotsIndicator(
    totalDots: Int,
    currentIndex: Int,
    activeColor: Color,
    inactiveColor: Color
) {
    Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
        repeat(totalDots) { index ->
            Box(
                modifier = Modifier
                    .padding(4.dp)
                    .size(if (index == currentIndex) 10.dp else 8.dp)
                    .clip(CircleShape)
                    .background(if (index == currentIndex) activeColor else inactiveColor)
            )
        }
    }
}