package com.exemple.stockagecompose.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.exemple.stockagecompose.data.ReglagesDataStore
import kotlinx.coroutines.launch

/**
 * Exemple 2 : DataStore Preferences
 * Les valeurs sont lues via un Flow et l'écran se met à jour tout seul.
 */
@Composable
fun DataStoreScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val reglages = remember { ReglagesDataStore(context) }
    val scope = rememberCoroutineScope()

    // collectAsState transforme le Flow en état Compose
    val prenomEnregistre by reglages.prenom.collectAsState(initial = "")
    val compteur by reglages.compteur.collectAsState(initial = 0)

    var saisie by remember { mutableStateOf("") }

    EcranDemo(titre = "DataStore", onBack = onBack) {
        Text(
            if (prenomEnregistre.isEmpty()) "Bonjour !" else "Bonjour $prenomEnregistre !",
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(
            value = saisie,
            onValueChange = { saisie = it },
            label = { Text("Votre prénom") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        Button(
            onClick = { scope.launch { reglages.enregistrerPrenom(saisie) } },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Enregistrer le prénom") }

        Spacer(Modifier.height(32.dp))
        Text("Compteur enregistré : $compteur", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { scope.launch { reglages.incrementer() } }) { Text("+1") }
            OutlinedButton(onClick = { scope.launch { reglages.remettreAZero() } }) { Text("Remettre à 0") }
        }
    }
}
