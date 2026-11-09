package com.exemple.mesrecettes.exemple2

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.exemple.mesrecettes.data.Recette
import com.exemple.mesrecettes.data.recettes
import com.exemple.mesrecettes.ui.BarreDuHaut
import com.exemple.mesrecettes.ui.ListeDeRecettes

@Composable
fun EcranListe(onRecetteClique: (Recette) -> Unit) {
    Scaffold(topBar = { BarreDuHaut("Mes recettes") }) { padding ->
        ListeDeRecettes(recettes = recettes, onRecetteClique = onRecetteClique, contentPadding = padding)
    }
}

@Composable
fun EcranDetail(recette: Recette, onRetour: () -> Unit, onVoirAuteur: () -> Unit) {
    Scaffold(topBar = { BarreDuHaut(recette.nom, onRetour = onRetour) }) { padding ->
        Column(
            Modifier.padding(padding).fillMaxSize().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
        ) {
            Text(recette.emoji, fontSize = 96.sp)
            Text(recette.nom, style = MaterialTheme.typography.headlineMedium)
            Text("⏱ ${recette.duree} min · ${recette.difficulte}")
            Text(recette.description, textAlign = TextAlign.Center)
            FilledTonalButton(onClick = onVoirAuteur) { Text("Voir l'auteur") }
        }
    }
}

// Exercice 2
@Composable
fun EcranAuteur(nom: String, onRetour: () -> Unit) {
    Scaffold(topBar = { BarreDuHaut("Auteur", onRetour = onRetour) }) { padding ->
        Column(
            Modifier.padding(padding).fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
        ) {
            Text("👤", fontSize = 80.sp)
            Text(nom, style = MaterialTheme.typography.headlineMedium)
            Text("${recettes.count { it.auteur == nom }} recette(s) publiée(s)")
        }
    }
}
