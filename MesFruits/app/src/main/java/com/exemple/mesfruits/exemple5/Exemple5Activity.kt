package com.exemple.mesfruits.exemple5

import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.os.bundleOf
import androidx.fragment.compose.AndroidFragment

/**
 * Chapitre 7 : un écran 100 % Compose qui affiche un ancien Fragment.
 * L'Activity doit hériter de FragmentActivity (AppCompatActivity en hérite).
 */
class Exemple5Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme { EcranAccueil() }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EcranAccueil() {
    val context = LocalContext.current
    Scaffold(topBar = {
        TopAppBar(title = { Text("Écran 100 % Compose") })
    }) { padding ->
        Column(
            Modifier
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Bienvenue !", style = MaterialTheme.typography.headlineSmall)
            Text("Cette partie est écrite en Compose.")

            // ⬇ Le Fragment, utilisé comme un Composable
            AndroidFragment<MeteoFragment>(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                arguments = bundleOf("ville" to "Rennes")
            )

            Button(
                onClick = { Toast.makeText(context, "Bouton Compose", Toast.LENGTH_SHORT).show() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Continuer")
            }
        }
    }
}
