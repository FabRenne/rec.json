package com.exemple.monjardin.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.LocalFlorist
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.exemple.monjardin.ui.theme.JardinTheme

data class Plante(val nom: String, val info: String)

val plantes = listOf(
    Plante("Monstera", "Arrosage : 1 fois / semaine"),
    Plante("Basilic", "Arrosage : tous les 2 jours"),
    Plante("Cactus", "Arrosage : 1 fois / mois"),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EcranJardin() {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Mon Jardin") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                ),
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {}) { Icon(Icons.Filled.Add, contentDescription = "Ajouter") }
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(selected = true, onClick = {}, icon = { Icon(Icons.Filled.LocalFlorist, null) }, label = { Text("Plantes") })
                NavigationBarItem(selected = false, onClick = {}, icon = { Icon(Icons.Outlined.CalendarMonth, null) }, label = { Text("Agenda") })
                NavigationBarItem(selected = false, onClick = {}, icon = { Icon(Icons.Outlined.Settings, null) }, label = { Text("Réglages") })
            }
        },
    ) { padding ->
        Column(Modifier.padding(padding).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("Bonjour !", style = MaterialTheme.typography.headlineMedium)
            Text(
                "3 plantes ont besoin de vous aujourd'hui.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            plantes.forEach { CartePlante(it) }
        }
    }
}

@Composable
fun CartePlante(p: Plante) {
    Card(shape = MaterialTheme.shapes.medium, modifier = Modifier.fillMaxWidth()) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Filled.LocalFlorist, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(32.dp))
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(p.nom, style = MaterialTheme.typography.titleMedium)
                Text(p.info, style = MaterialTheme.typography.bodyMedium)
            }
            FilledTonalButton(onClick = {}) { Text("Arroser") }
        }
    }
}

@Preview(name = "Clair", showBackground = true)
@Composable
fun ApercuClair() { JardinTheme(darkTheme = false) { EcranJardin() } }

@Preview(name = "Sombre", showBackground = true)
@Composable
fun ApercuSombre() { JardinTheme(darkTheme = true) { EcranJardin() } }
