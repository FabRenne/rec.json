package com.fabrenne.moutures

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.fabrenne.moutures.ui.MouturesScreen
import com.fabrenne.moutures.ui.theme.MouturesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MouturesTheme {
                MouturesScreen()
            }
        }
    }
}
