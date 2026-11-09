@file:OptIn(ExperimentalMaterial3Api::class)

package com.exemple.mesrecettes.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.exemple.mesrecettes.data.Recette

/*
 * Petits composants réutilisés par tous les exemples.
 * Ils ne savent RIEN de la navigation : ils reçoivent des lambdas.
 */

/** Barre du haut, avec une flèche « Retour » si onRetour n'est pas null. */
@Composable
fun BarreDuHaut(titre: String, onRetour: (() -> Unit)? = null) {
    TopAppBar(
        title = { Text(titre) },
        navigationIcon = {
            if (onRetour != null) {
                IconButton(onClick = onRetour) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Retour")
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    )
}

/** Une carte cliquable qui présente une recette. */
@Composable
fun CarteRecette(recette: Recette, onClick: () -> Unit, symbole: String = "›") {
    Card(onClick = onClick, modifier = Modifier.fillMaxWidth()) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(recette.emoji, fontSize = 32.sp)
            Spacer(Modifier.width(16.dp))
            Column(Modifier.weight(1f)) {
                Text(recette.nom, style = MaterialTheme.typography.titleMedium)
                Text("${recette.duree} min · ${recette.difficulte}", style = MaterialTheme.typography.bodySmall)
            }
            Text(symbole, style = MaterialTheme.typography.titleLarge)
        }
    }
}

/** Une liste de recettes. */
@Composable
fun ListeDeRecettes(
    recettes: List<Recette>,
    onRecetteClique: (Recette) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    symbole: String = "›",
) {
    LazyColumn(
        modifier = modifier.padding(horizontal = 16.dp),
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(recettes) { recette ->
            CarteRecette(recette, onClick = { onRecetteClique(recette) }, symbole = symbole)
        }
    }
}

/**
 * 5 étoiles. Si onChoix n'est pas null, on peut toucher une étoile pour choisir la note.
 */
@Composable
fun Etoiles(note: Int, taille: TextUnit = 40.sp, onChoix: ((Int) -> Unit)? = null) {
    Row {
        for (i in 1..5) {
            val couleur = if (i <= note) Color(0xFFF2B01E) else Color(0xFFC9C7D2)
            val modifier = if (onChoix != null) Modifier.clickable { onChoix(i) } else Modifier
            Text("★", color = couleur, fontSize = taille, modifier = modifier.padding(2.dp))
        }
    }
}
