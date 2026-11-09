package com.exemple.mesrecettes.exemple5

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.exemple.mesrecettes.data.Recette
import com.exemple.mesrecettes.data.recettes
import com.exemple.mesrecettes.ui.BarreDuHaut
import com.exemple.mesrecettes.ui.Etoiles
import com.exemple.mesrecettes.ui.ListeDeRecettes

@Composable
fun EcranListe(onRecetteClique: (Recette) -> Unit) {
    Scaffold(topBar = { BarreDuHaut("Mes recettes") }) { padding ->
        ListeDeRecettes(recettes = recettes, onRecetteClique = onRecetteClique, contentPadding = padding)
    }
}

@Composable
fun EcranDetailAvecNote(recette: Recette, note: Int, onNoter: () -> Unit, onRetour: () -> Unit) {
    Scaffold(topBar = { BarreDuHaut(recette.nom, onRetour = onRetour) }) { padding ->
        Column(
            Modifier.padding(padding).fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
        ) {
            Text(recette.emoji, fontSize = 96.sp)
            Text(recette.nom, style = MaterialTheme.typography.headlineMedium)
            if (note == 0) {
                Text("Vous n'avez pas encore noté cette recette.")
            } else {
                Text("Votre note :")
                Etoiles(note = note)
            }
            Button(onClick = onNoter) { Text("Noter cette recette") }
        }
    }
}

@Composable
fun EcranNoter(recette: Recette, onValider: (Int) -> Unit, onRetour: () -> Unit) {
    var note by rememberSaveable { mutableIntStateOf(3) }
    Scaffold(topBar = { BarreDuHaut("Noter « ${recette.nom} »", onRetour = onRetour) }) { padding ->
        Column(
            Modifier.padding(padding).fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically)
        ) {
            Text("Touchez les étoiles pour choisir votre note :")
            Etoiles(note = note, taille = 52.sp, onChoix = { note = it })
            Text("$note / 5", style = MaterialTheme.typography.headlineMedium)
            Button(onClick = { onValider(note) }) { Text("Valider") }
        }
    }
}
