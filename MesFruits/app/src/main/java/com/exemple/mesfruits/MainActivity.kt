package com.exemple.mesfruits

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.exemple.mesfruits.exemple1.Exemple1Activity
import com.exemple.mesfruits.exemple5.Exemple5Activity
import com.exemple.mesfruits.exemple6.Exemple6Activity
import com.exemple.mesfruits.fragments.FragmentsActivity

/**
 * Menu principal : un bouton par exemple du cours.
 * (Cette Activity elle-même est en 100 % Compose.)
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MaterialTheme { EcranMenu() } }
    }
}

private data class Exemple(
    val titre: String,
    val description: String,
    val activite: Class<out Activity>,
)

private val exemples = listOf(
    Exemple("Exemple 1 · Chapitre 3", "Afficher du Compose dans un Fragment (ComposeView)", Exemple1Activity::class.java),
    Exemple("Exemples 2, 3 et 4 · Chapitres 4 à 6", "Navigation entre Fragments, arguments et ViewModel partagé", FragmentsActivity::class.java),
    Exemple("Exemple 5 · Chapitre 7", "Afficher un ancien Fragment dans Compose (AndroidFragment)", Exemple5Activity::class.java),
    Exemple("Exemple 6 · Chapitre 8", "100 % Compose avec Navigation Compose, sans Fragment", Exemple6Activity::class.java),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EcranMenu() {
    val context = LocalContext.current
    Scaffold(topBar = { TopAppBar(title = { Text("Cours : Fragments & Compose") }) }) { padding ->
        LazyColumn(
            contentPadding = padding,
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            items(exemples) { ex ->
                Card(
                    onClick = { context.startActivity(Intent(context, ex.activite)) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text(ex.titre, style = MaterialTheme.typography.titleMedium)
                        Text(ex.description, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}
