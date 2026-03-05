package com.example.karigar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.karigar.navigation.KarigarNavGraph
import com.example.karigar.ui.theme.KarigarTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KarigarTheme {
                KarigarNavGraph()

            }
        }
    }
}