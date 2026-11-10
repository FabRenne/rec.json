package com.exemple.stockagecompose.ui

import android.content.Context
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.edit

/**
 * Exemple 1 : SharedPreferences
 * On enregistre un pseudo (String) et une option (Boolean).
 */
@Composable
fun PreferencesScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    // 1) On récupère le "fichier" de préférences nommé "mes_reglages"
    val prefs = remember { context.getSharedPreferences("mes_reglages", Context.MODE_PRIVATE) }

    // 2) On lit les valeurs déjà enregistrées (ou une valeur par défaut)
    var pseudo by remember { mutableStateOf(prefs.getString("pseudo", "") ?: "") }
    var pseudoEnregistre by remember { mutableStateOf(pseudo) }
    var notifications by remember { mutableStateOf(prefs.getBoolean("notifications", true)) }

    EcranDemo(titre = "SharedPreferences", onBack = onBack) {
        OutlinedTextField(
            value = pseudo,
            onValueChange = { pseudo = it },
            label = { Text("Votre pseudo") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(12.dp))
        Button(
            onClick = {
                // 3) On écrit la valeur
                prefs.edit { putString("pseudo", pseudo) }
                pseudoEnregistre = pseudo
            },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Enregistrer") }

        Spacer(Modifier.height(24.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Recevoir des notifications", modifier = Modifier.weight(1f))
            Switch(
                checked = notifications,
                onCheckedChange = {
                    notifications = it
                    prefs.edit { putBoolean("notifications", it) } // sauvegarde immédiate
                }
            )
        }
        Spacer(Modifier.height(24.dp))
        Text("Valeur enregistrée : ${pseudoEnregistre.ifEmpty { "(aucune)" }}",
            style = MaterialTheme.typography.bodyLarge)
        Text("Fermez puis relancez l'application : la valeur est toujours là !",
            style = MaterialTheme.typography.bodySmall)
    }
}
