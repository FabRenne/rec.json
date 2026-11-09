package com.exemple.mesrecettes.exemple4

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.exemple.mesrecettes.data.recettes
import com.exemple.mesrecettes.ui.ListeDeRecettes

/*
 * Ici, les écrans n'ont PAS leur propre Scaffold :
 * la barre du haut et la barre du bas sont communes (dans AppExemple4).
 */

@Composable
fun EcranRecettes() {
    ListeDeRecettes(recettes = recettes, onRecetteClique = {}, modifier = Modifier.padding(top = 8.dp))
}

@Composable
fun EcranFavoris() {
    val favoris = recettes.filter { it.id == 1 || it.id == 5 }
    ListeDeRecettes(recettes = favoris, onRecetteClique = {}, modifier = Modifier.padding(top = 8.dp), symbole = "♥")
}

@Composable
fun EcranProfil() {
    // rememberSaveable + restoreState = l'interrupteur garde sa position quand on change d'onglet
    var notifications by rememberSaveable { mutableStateOf(true) }
    Column(
        Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("👩‍🍳", fontSize = 72.sp)
        Text("Léa Martin", style = MaterialTheme.typography.headlineSmall)
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Notifications")
            Switch(checked = notifications, onCheckedChange = { notifications = it })
        }
        HorizontalDivider()
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Recettes publiées"); Text("12")
        }
    }
}
