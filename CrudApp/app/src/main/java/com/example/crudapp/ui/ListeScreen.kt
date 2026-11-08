package com.example.crudapp.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.crudapp.data.Enregistrement

enum class ModeListe { MODIFIER, SUPPRIMER }

/**
 * Liste des enregistrements.
 * - MODIFIER  : un appui ouvre le formulaire de modification.
 * - SUPPRIMER : un appui (ou l'icône corbeille) demande confirmation puis supprime.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListeScreen(
    vm: CrudViewModel,
    mode: ModeListe,
    onChoisir: (Long) -> Unit,
    onRetour: () -> Unit
) {
    val liste by vm.enregistrements.collectAsStateWithLifecycle()
    var aSupprimer by remember { mutableStateOf<Enregistrement?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (mode == ModeListe.MODIFIER) "Choisir l'enregistrement à modifier"
                        else "Supprimer un enregistrement"
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onRetour) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Retour")
                    }
                }
            )
        }
    ) { padding ->
        if (liste.isEmpty()) {
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("Aucun enregistrement")
            }
        } else {
            LazyColumn(
                Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                items(liste, key = { it.id }) { e ->
                    ListItem(
                        headlineContent = { Text(e.nom) },
                        supportingContent = { Text(e.date) },
                        trailingContent = {
                            if (mode == ModeListe.MODIFIER) {
                                Icon(Icons.Default.Edit, contentDescription = "Modifier")
                            } else {
                                IconButton(onClick = { aSupprimer = e }) {
                                    Icon(
                                        Icons.Default.Delete,
                                        contentDescription = "Supprimer",
                                        tint = MaterialTheme.colorScheme.error
                                    )
                                }
                            }
                        },
                        modifier = Modifier.clickable {
                            if (mode == ModeListe.MODIFIER) onChoisir(e.id) else aSupprimer = e
                        }
                    )
                    HorizontalDivider()
                }
            }
        }
    }

    aSupprimer?.let { e ->
        AlertDialog(
            onDismissRequest = { aSupprimer = null },
            title = { Text("Confirmer la suppression") },
            text = { Text("Supprimer « ${e.nom} » du ${e.date} ?") },
            confirmButton = {
                TextButton(onClick = {
                    vm.supprimer(e.id)
                    aSupprimer = null
                }) { Text("Supprimer", color = MaterialTheme.colorScheme.error) }
            },
            dismissButton = {
                TextButton(onClick = { aSupprimer = null }) { Text("Annuler") }
            }
        )
    }
}
