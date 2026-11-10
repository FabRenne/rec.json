package com.exemple.stockagecompose.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

private data class Choix(val route: String, val titre: String, val description: String, val icone: ImageVector)

private val choix = listOf(
    Choix("preferences", "1. SharedPreferences", "Petites valeurs clé / valeur", Icons.Filled.Settings),
    Choix("datastore", "2. DataStore", "Le remplaçant moderne, avec Flow", Icons.Filled.Tune),
    Choix("fichier", "3. Fichier interne", "Lire et écrire un fichier texte", Icons.Filled.Description),
    Choix("room", "4. Room", "Une vraie base de données SQLite", Icons.Filled.Storage),
)

@Composable
fun AccueilScreen(onNavigate: (String) -> Unit) {
    EcranDemo(titre = "Stockage Compose", onBack = null) {
        Text(
            "Choisissez une façon d'enregistrer des données :",
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(Modifier.padding(8.dp))
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            choix.forEach { c ->
                Card(onClick = { onNavigate(c.route) }, modifier = Modifier.fillMaxWidth()) {
                    Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(c.icone, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Spacer(Modifier.width(16.dp))
                        Column {
                            Text(c.titre, style = MaterialTheme.typography.titleMedium)
                            Text(c.description, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
        }
    }
}
