@file:OptIn(ExperimentalMaterial3Api::class)

package com.exemple.mesrecettes

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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.exemple.mesrecettes.exemple1.Exemple1Activity
import com.exemple.mesrecettes.exemple2.Exemple2Activity
import com.exemple.mesrecettes.exemple3.Exemple3Activity
import com.exemple.mesrecettes.exemple4.Exemple4Activity
import com.exemple.mesrecettes.exemple5.Exemple5Activity
import com.exemple.mesrecettes.ui.BarreDuHaut

/** Menu principal : un bouton par exemple du cours. */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MaterialTheme { EcranMenu() } }
    }
}

private data class Exemple(val titre: String, val description: String, val activite: Class<out Activity>)

private val exemples = listOf(
    Exemple("Exemple 1 · Chapitre 3", "Deux écrans : Accueil → Profil", Exemple1Activity::class.java),
    Exemple("Exemple 2 · Chapitre 4", "Passer un argument : liste → détail d'une recette", Exemple2Activity::class.java),
    Exemple("Exemple 3 · Chapitre 5", "Gérer la pile : connexion avec popUpTo", Exemple3Activity::class.java),
    Exemple("Exemple 4 · Chapitre 6", "Barre de navigation du bas (onglets)", Exemple4Activity::class.java),
    Exemple("Exemple 5 · Chapitre 7", "Renvoyer un résultat : noter une recette", Exemple5Activity::class.java),
)

@Composable
private fun EcranMenu() {
    val context = LocalContext.current
    Scaffold(topBar = { BarreDuHaut("Cours : la navigation en Compose") }) { padding ->
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
