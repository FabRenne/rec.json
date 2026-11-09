package com.exemple.mesrecettes.exemple1

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.exemple.mesrecettes.ui.BarreDuHaut

@Composable
fun EcranAccueil(onVoirProfil: () -> Unit) {
    Scaffold(topBar = { BarreDuHaut("Accueil") }) { padding ->
        Column(
            Modifier.padding(padding).fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
        ) {
            Text("🏠", fontSize = 96.sp)
            Text("Bienvenue !", style = MaterialTheme.typography.headlineMedium)
            Text("Ceci est le premier écran\n(destination Accueil).", textAlign = TextAlign.Center)
            Button(onClick = onVoirProfil) { Text("Voir mon profil") }
        }
    }
}

@Composable
fun EcranProfil(onRetour: () -> Unit, onParametres: () -> Unit) {
    Scaffold(topBar = { BarreDuHaut("Profil", onRetour = onRetour) }) { padding ->
        Column(
            Modifier.padding(padding).fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
        ) {
            Text("👩‍🍳", fontSize = 80.sp)
            Text("Léa Martin", style = MaterialTheme.typography.headlineMedium)
            Text("Cuisinière amatrice 🍳")
            FilledTonalButton(onClick = onRetour) { Text("← Retour") }
            Button(onClick = onParametres) { Text("Paramètres") }
        }
    }
}

// Exercice 1
@Composable
fun EcranParametres(onRetour: () -> Unit) {
    var notifications by rememberSaveable { mutableStateOf(true) }
    Scaffold(topBar = { BarreDuHaut("Paramètres", onRetour = onRetour) }) { padding ->
        Column(Modifier.padding(padding).padding(16.dp)) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Notifications")
                Switch(checked = notifications, onCheckedChange = { notifications = it })
            }
        }
    }
}
