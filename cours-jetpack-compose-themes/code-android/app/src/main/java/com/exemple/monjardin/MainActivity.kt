package com.exemple.monjardin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.exemple.monjardin.ui.EcranJardin
import com.exemple.monjardin.ui.theme.JardinTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JardinTheme {          // Tout ce qui est à l'intérieur hérite du thème
                EcranJardin()
            }
        }
    }
}
