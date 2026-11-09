package com.exemple.mesfruits.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.exemple.mesfruits.data.Fruit
import com.exemple.mesfruits.data.fruits

/*
 * Tous les écrans de l'application, écrits en Compose.
 * Règle d'or : ces fonctions NE CONNAISSENT PAS les Fragments.
 * Elles reçoivent des lambdas (onFruitClique, onRetour...) et c'est
 * l'appelant (Fragment ou NavHost) qui décide quoi faire.
 */

// ---------------------------------------------------------------- Chapitre 3
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EcranPremierFragment() {
    // rememberSaveable : le compteur survit à la rotation (chapitre 9)
    var clics by rememberSaveable { mutableIntStateOf(0) }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Mon premier Fragment") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Bonjour ! 👋", style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(8.dp))
            Text(
                "Ce texte est dessiné par Jetpack Compose, à l'intérieur d'un Fragment.",
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(24.dp))
            Text("Nombre de clics")
            Text("$clics", style = MaterialTheme.typography.displayLarge)
            Button(onClick = { clics++ }) {
                Text("Cliquer ici")
            }
        }
    }
}

// ---------------------------------------------------------------- Chapitre 4
/**
 * @param nbDansPanier si différent de null, affiche l'icône du panier (chapitre 6).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EcranListe(
    onFruitClique: (Fruit) -> Unit,
    titre: String = "Mes fruits",
    nbDansPanier: Int? = null,
    onPanier: () -> Unit = {},
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(titre) },
                actions = {
                    if (nbDansPanier != null) {
                        IconButton(onClick = onPanier) {
                            BadgedBox(badge = {
                                if (nbDansPanier > 0) Badge { Text("$nbDansPanier") }
                            }) {
                                Icon(Icons.Filled.ShoppingCart, contentDescription = "Panier")
                            }
                        }
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            contentPadding = padding,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            items(fruits) { fruit ->
                Card(onClick = { onFruitClique(fruit) }, modifier = Modifier.fillMaxWidth()) {
                    Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text(fruit.emoji, fontSize = 32.sp)
                        Spacer(Modifier.width(16.dp))
                        Column {
                            Text(fruit.nom, style = MaterialTheme.typography.titleMedium)
                            Text(fruit.description, style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        }
    }
}

// ---------------------------------------------------------------- Chapitre 5
/**
 * @param onAjouterPanier si non null, affiche le bouton « Ajouter au panier » (chapitre 6).
 * @param onHasard si non null, affiche le bouton « fruit au hasard » (exercices 2 et 3).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EcranDetail(
    fruit: Fruit,
    onRetour: () -> Unit,
    onAjouterPanier: (() -> Unit)? = null,
    onHasard: (() -> Unit)? = null,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Détail") },
                navigationIcon = {
                    IconButton(onClick = onRetour) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Retour")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            Modifier
                .padding(padding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(fruit.emoji, fontSize = 96.sp)
            Text(fruit.nom, style = MaterialTheme.typography.headlineMedium)
            Text("Vous avez choisi ce fruit sur l'écran précédent.")
            Spacer(Modifier.height(16.dp))
            if (onAjouterPanier != null) {
                Button(onClick = onAjouterPanier) { Text("Ajouter au panier") }
            }
            if (onHasard != null) {
                Button(onClick = onHasard) { Text("Voir un autre fruit au hasard") }
            }
            FilledTonalButton(onClick = onRetour) { Text("← Retour à la liste") }
        }
    }
}

// ---------------------------------------------------------------- Chapitre 6
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EcranPanier(fruits: List<Fruit>, onVider: () -> Unit, onRetour: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mon panier (${fruits.size})") },
                navigationIcon = {
                    IconButton(onClick = onRetour) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Retour")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (fruits.isEmpty()) {
                Text("Le panier est vide. Ajoutez des fruits depuis l'écran de détail !")
            } else {
                Text("Fruits ajoutés (partagés entre les Fragments grâce au ViewModel) :")
                fruits.forEach { fruit ->
                    Card(Modifier.fillMaxWidth()) {
                        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                            Text(fruit.emoji, fontSize = 28.sp)
                            Spacer(Modifier.width(16.dp))
                            Text(fruit.nom, style = MaterialTheme.typography.titleMedium)
                        }
                    }
                }
                FilledTonalButton(
                    onClick = onVider,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) { Text("Vider le panier") }
            }
        }
    }
}

// ---------------------------------------------------------------- Aperçus
// Visibles dans Android Studio (onglet « Split » ou « Design ») sans lancer l'appli.
@Preview(showBackground = true)
@Composable
private fun ApercuListe() {
    MaterialTheme { EcranListe(onFruitClique = {}) }
}

@Preview(showBackground = true)
@Composable
private fun ApercuDetail() {
    MaterialTheme { EcranDetail(fruit = fruits[3], onRetour = {}) }
}
