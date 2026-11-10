package com.exemple.stockagecompose.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.exemple.stockagecompose.data.AppDatabase
import com.exemple.stockagecompose.data.Note
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Exemple 4 : Room
 * Une liste de notes enregistrée dans une base SQLite.
 */
@Composable
fun RoomScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val dao = remember { AppDatabase.get(context).noteDao() }
    val scope = rememberCoroutineScope()

    val notes by dao.toutesLesNotes().collectAsState(initial = emptyList())
    var saisie by remember { mutableStateOf("") }
    val format = remember { SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.FRANCE) }

    EcranDemo(titre = "Room", onBack = onBack) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = saisie,
                onValueChange = { saisie = it },
                label = { Text("Nouvelle note") },
                modifier = Modifier.weight(1f)
            )
            Spacer(Modifier.padding(4.dp))
            Button(
                enabled = saisie.isNotBlank(),
                onClick = {
                    scope.launch { dao.ajouter(Note(titre = saisie)) }
                    saisie = ""
                }
            ) { Text("Ajouter") }
        }
        Spacer(Modifier.height(12.dp))
        Text("${notes.size} note(s) dans la base", style = MaterialTheme.typography.labelLarge)
        Spacer(Modifier.height(8.dp))
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(notes, key = { it.id }) { note ->
                Card(Modifier.fillMaxWidth()) {
                    Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        Column(Modifier.weight(1f)) {
                            Text(note.titre, style = MaterialTheme.typography.titleMedium)
                            Text(format.format(Date(note.dateCreation)),
                                style = MaterialTheme.typography.bodySmall)
                        }
                        IconButton(onClick = { scope.launch { dao.supprimer(note) } }) {
                            Icon(Icons.Filled.Delete, contentDescription = "Supprimer")
                        }
                    }
                }
            }
        }
    }
}
