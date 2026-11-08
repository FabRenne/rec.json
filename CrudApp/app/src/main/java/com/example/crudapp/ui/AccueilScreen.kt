package com.example.crudapp.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccueilScreen(
    vm: CrudViewModel,
    onAjouter: () -> Unit,
    onModifier: () -> Unit,
    onSupprimer: () -> Unit
) {
    val nombre by vm.nombre.collectAsStateWithLifecycle()

    Scaffold(topBar = { TopAppBar(title = { Text("CRUD SQLite") }) }) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Nombre d'enregistrements", style = MaterialTheme.typography.titleMedium)
                    Text(
                        text = nombre.toString(),
                        style = MaterialTheme.typography.displayLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(Modifier.height(32.dp))

            BoutonAction("Ajouter", Icons.Default.Add, onAjouter)
            Spacer(Modifier.height(12.dp))
            BoutonAction("Modifier", Icons.Default.Edit, onModifier, actif = nombre > 0)
            Spacer(Modifier.height(12.dp))
            BoutonAction(
                "Supprimer", Icons.Default.Delete, onSupprimer, actif = nombre > 0,
                couleurs = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError
                )
            )
        }
    }
}

@Composable
private fun BoutonAction(
    texte: String,
    icone: ImageVector,
    onClick: () -> Unit,
    actif: Boolean = true,
    couleurs: androidx.compose.material3.ButtonColors = ButtonDefaults.buttonColors()
) {
    Button(
        onClick = onClick,
        enabled = actif,
        colors = couleurs,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
    ) {
        Icon(icone, contentDescription = null, modifier = Modifier.size(20.dp))
        Spacer(Modifier.size(8.dp))
        Text(texte)
    }
}
