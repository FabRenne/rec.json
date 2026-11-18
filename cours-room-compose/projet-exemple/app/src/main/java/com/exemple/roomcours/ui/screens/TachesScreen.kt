package com.exemple.roomcours.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.exemple.roomcours.data.local.entity.Categorie
import com.exemple.roomcours.data.local.entity.Priorite
import com.exemple.roomcours.data.local.entity.Tache
import com.exemple.roomcours.ui.TacheViewModel

/** Composable "avec état" : relie le ViewModel à l'UI. */
@Composable
fun TachesRoute(viewModel: TacheViewModel = viewModel(factory = TacheViewModel.Factory)) {
    // Le Flow Room -> StateFlow -> State Compose (respecte le cycle de vie)
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    TachesScreen(
        taches = state.taches,
        categories = state.categories,
        recherche = state.recherche,
        restantes = state.restantes,
        chargement = state.chargement,
        onRechercheChange = viewModel::onRechercheChange,
        onAjouter = viewModel::ajouter,
        onBasculer = { viewModel.basculer(it) },
        onSupprimer = { viewModel.supprimer(it) },
        onNettoyer = { viewModel.nettoyer() }
    )
}

/** Composable "sans état" : facile à prévisualiser et à tester. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TachesScreen(
    taches: List<Tache>,
    categories: List<Categorie>,
    recherche: String,
    restantes: Int,
    chargement: Boolean,
    onRechercheChange: (String) -> Unit,
    onAjouter: (String, Priorite, Long?) -> Unit,
    onBasculer: (Tache) -> Unit,
    onSupprimer: (Tache) -> Unit,
    onNettoyer: () -> Unit
) {
    var dialogueOuvert by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mes tâches ($restantes restantes)") },
                actions = {
                    IconButton(onClick = onNettoyer) {
                        Icon(Icons.Default.DeleteSweep, contentDescription = "Supprimer les terminées")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { dialogueOuvert = true }) {
                Icon(Icons.Default.Add, contentDescription = "Ajouter")
            }
        }
    ) { padding ->
        Column(Modifier.padding(padding).fillMaxSize()) {
            OutlinedTextField(
                value = recherche,
                onValueChange = onRechercheChange,
                leadingIcon = { Icon(Icons.Default.Search, null) },
                placeholder = { Text("Rechercher…") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth().padding(16.dp, 8.dp)
            )

            when {
                chargement -> Box(Modifier.fillMaxSize(), Alignment.Center) {
                    CircularProgressIndicator()
                }
                taches.isEmpty() -> Box(Modifier.fillMaxSize(), Alignment.Center) {
                    Text("Aucune tâche", style = MaterialTheme.typography.bodyLarge)
                }
                else -> LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // key = id : Compose réutilise correctement les éléments
                    items(taches, key = { it.id }) { tache ->
                        TacheItem(tache, onBasculer, onSupprimer)
                    }
                }
            }
        }
    }

    if (dialogueOuvert) {
        AjoutTacheDialog(
            categories = categories,
            onFermer = { dialogueOuvert = false },
            onValider = { titre, prio, cat ->
                onAjouter(titre, prio, cat)
                dialogueOuvert = false
            }
        )
    }
}

@Composable
fun TacheItem(tache: Tache, onBasculer: (Tache) -> Unit, onSupprimer: (Tache) -> Unit) {
    Card(Modifier.fillMaxWidth()) {
        Row(Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = tache.estTerminee, onCheckedChange = { onBasculer(tache) })
            Column(Modifier.weight(1f)) {
                Text(
                    tache.titre,
                    style = MaterialTheme.typography.titleMedium,
                    textDecoration = if (tache.estTerminee) TextDecoration.LineThrough else null
                )
                Text(
                    "Priorité : ${tache.priorite.name.lowercase()}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            IconButton(onClick = { onSupprimer(tache) }) {
                Icon(Icons.Default.Delete, contentDescription = "Supprimer")
            }
        }
    }
}

@Composable
fun AjoutTacheDialog(
    categories: List<Categorie>,
    onFermer: () -> Unit,
    onValider: (String, Priorite, Long?) -> Unit
) {
    var titre by rememberSaveable { mutableStateOf("") }
    var priorite by rememberSaveable { mutableStateOf(Priorite.NORMALE) }
    var categorieId by rememberSaveable { mutableStateOf<Long?>(null) }

    AlertDialog(
        onDismissRequest = onFermer,
        title = { Text("Nouvelle tâche") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(titre, { titre = it }, label = { Text("Titre") })
                Row {
                    Priorite.entries.forEach { p ->
                        FilterChip(
                            selected = p == priorite,
                            onClick = { priorite = p },
                            label = { Text(p.name.lowercase()) }
                        )
                        Spacer(Modifier.width(4.dp))
                    }
                }
                Row {
                    categories.forEach { c ->
                        FilterChip(
                            selected = c.id == categorieId,
                            onClick = { categorieId = if (categorieId == c.id) null else c.id },
                            label = { Text(c.nom) }
                        )
                        Spacer(Modifier.width(4.dp))
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { onValider(titre, priorite, categorieId) }, enabled = titre.isNotBlank()) {
                Text("Ajouter")
            }
        },
        dismissButton = { TextButton(onClick = onFermer) { Text("Annuler") } }
    )
}
