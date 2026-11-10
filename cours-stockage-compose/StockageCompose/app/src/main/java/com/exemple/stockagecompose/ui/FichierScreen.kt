package com.exemple.stockagecompose.ui

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import java.io.File

private const val NOM_FICHIER = "journal.txt"

// Écrit le texte dans le stockage interne de l'application
fun ecrireFichier(context: Context, texte: String) {
    context.openFileOutput(NOM_FICHIER, Context.MODE_PRIVATE).use { flux ->
        flux.write(texte.toByteArray())
    }
}

// Lit le fichier s'il existe, sinon renvoie une chaîne vide
fun lireFichier(context: Context): String {
    val fichier = File(context.filesDir, NOM_FICHIER)
    return if (fichier.exists()) fichier.readText() else ""
}

/**
 * Exemple 3 : fichier texte dans le stockage interne (context.filesDir)
 */
@Composable
fun FichierScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    var texte by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    EcranDemo(titre = "Fichier interne", onBack = onBack) {
        OutlinedTextField(
            value = texte,
            onValueChange = { texte = it },
            label = { Text("Mon journal") },
            modifier = Modifier.fillMaxWidth().height(220.dp)
        )
        Spacer(Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = {
                ecrireFichier(context, texte)
                message = "Enregistré dans $NOM_FICHIER (${texte.length} caractères)"
            }) { Text("Enregistrer") }
            OutlinedButton(onClick = {
                texte = lireFichier(context)
                message = "Fichier relu"
            }) { Text("Charger") }
        }
        Spacer(Modifier.height(16.dp))
        Text(message, style = MaterialTheme.typography.bodyMedium)
        Text("Emplacement : ${context.filesDir}/$NOM_FICHIER", style = MaterialTheme.typography.bodySmall)
    }
}
