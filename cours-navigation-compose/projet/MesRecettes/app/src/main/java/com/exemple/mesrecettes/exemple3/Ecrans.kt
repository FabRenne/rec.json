package com.exemple.mesrecettes.exemple3

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.exemple.mesrecettes.ui.BarreDuHaut

@Composable
fun EcranConnexion(onConnecte: (String) -> Unit) {
    var prenom by rememberSaveable { mutableStateOf("") }
    var motDePasse by rememberSaveable { mutableStateOf("") }

    Scaffold(topBar = { BarreDuHaut("Connexion") }) { padding ->
        Column(
            Modifier.padding(padding).fillMaxSize().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
        ) {
            Text("🔐", fontSize = 72.sp)
            OutlinedTextField(
                value = prenom,
                onValueChange = { prenom = it },
                label = { Text("Prénom") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = motDePasse,
                onValueChange = { motDePasse = it },
                label = { Text("Mot de passe") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth()
            )
            Button(
                onClick = { onConnecte(prenom.ifBlank { "invité" }) },
                modifier = Modifier.fillMaxWidth()
            ) { Text("Se connecter") }
            Text(
                "Après la connexion, le bouton Retour\nne doit PAS revenir ici.",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
fun EcranAccueilConnecte(prenom: String, onDeconnexion: () -> Unit) {
    Scaffold(topBar = { BarreDuHaut("Bonjour $prenom 👋") }) { padding ->
        Column(
            Modifier.padding(padding).fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
        ) {
            Text("🎉", fontSize = 96.sp)
            Text("Vous êtes connecté(e)", style = MaterialTheme.typography.headlineSmall)
            Text(
                "Appuyez sur Retour : l'application se ferme,\ncar l'écran Connexion a été retiré de la pile.",
                textAlign = TextAlign.Center
            )
            FilledTonalButton(onClick = onDeconnexion) { Text("Se déconnecter") }
        }
    }
}
