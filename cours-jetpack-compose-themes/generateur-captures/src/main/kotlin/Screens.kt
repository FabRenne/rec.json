import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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
            Text("3 plantes ont besoin de vous aujourd'hui.", style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
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

// ---------- Palette ----------
@Composable
fun Pastille(nom: String, fond: Color, texte: Color, modifier: Modifier = Modifier) {
    Box(modifier.height(52.dp).background(fond).padding(horizontal = 8.dp), contentAlignment = Alignment.CenterStart) {
        Text(nom, color = texte, fontSize = 12.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun EcranPalette() {
    val c = MaterialTheme.colorScheme
    val paires = listOf(
        Triple("primary", c.primary, c.onPrimary) to Triple("onPrimary", c.onPrimary, c.primary),
        Triple("primaryContainer", c.primaryContainer, c.onPrimaryContainer) to Triple("onPrimaryContainer", c.onPrimaryContainer, c.primaryContainer),
        Triple("secondary", c.secondary, c.onSecondary) to Triple("onSecondary", c.onSecondary, c.secondary),
        Triple("secondaryContainer", c.secondaryContainer, c.onSecondaryContainer) to Triple("onSecondaryContainer", c.onSecondaryContainer, c.secondaryContainer),
        Triple("tertiary", c.tertiary, c.onTertiary) to Triple("onTertiary", c.onTertiary, c.tertiary),
        Triple("tertiaryContainer", c.tertiaryContainer, c.onTertiaryContainer) to Triple("onTertiaryContainer", c.onTertiaryContainer, c.tertiaryContainer),
        Triple("error", c.error, c.onError) to Triple("onError", c.onError, c.error),
        Triple("errorContainer", c.errorContainer, c.onErrorContainer) to Triple("onErrorContainer", c.onErrorContainer, c.errorContainer),
        Triple("background", c.background, c.onBackground) to Triple("onBackground", c.onBackground, c.background),
        Triple("surface", c.surface, c.onSurface) to Triple("onSurface", c.onSurface, c.surface),
        Triple("surfaceVariant", c.surfaceVariant, c.onSurfaceVariant) to Triple("onSurfaceVariant", c.onSurfaceVariant, c.surfaceVariant),
        Triple("outline", c.outline, c.surface) to Triple("inverseSurface", c.inverseSurface, c.inverseOnSurface),
    )
    Surface(color = c.surface) {
        Column(Modifier.fillMaxSize().padding(12.dp)) {
            Text("MaterialTheme.colorScheme", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(bottom = 8.dp))
            Column(Modifier.border(1.dp, c.outline)) {
                paires.forEach { (a, b) ->
                    Row {
                        Pastille(a.first, a.second, a.third, Modifier.weight(1f))
                        Pastille(b.first, b.second, b.third, Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

// ---------- Typographie ----------
@Composable
fun EcranTypo() {
    val t = MaterialTheme.typography
    val styles = listOf(
        "displayLarge" to t.displayLarge, "headlineMedium" to t.headlineMedium, "titleLarge" to t.titleLarge,
        "titleMedium" to t.titleMedium, "bodyLarge" to t.bodyLarge, "bodyMedium" to t.bodyMedium,
        "labelLarge" to t.labelLarge, "labelSmall" to t.labelSmall,
    )
    Surface(Modifier.fillMaxSize()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            styles.forEach { (nom, style) ->
                Text(nom, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
                Text(if (nom == "displayLarge") "Jardin" else "Mon beau jardin", style = style)
            }
        }
    }
}

// ---------- Formes ----------
@Composable
fun EchantillonForme(nom: String, shape: Shape) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(Modifier.size(120.dp, 72.dp).clip(shape).background(MaterialTheme.colorScheme.primaryContainer).border(2.dp, MaterialTheme.colorScheme.primary, shape))
        Text(nom, style = MaterialTheme.typography.labelLarge, modifier = Modifier.padding(top = 4.dp))
    }
}

@Composable
fun EcranFormes() {
    val s = MaterialTheme.shapes
    Surface(Modifier.fillMaxSize()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) { EchantillonForme("extraSmall", s.extraSmall); EchantillonForme("small", s.small) }
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) { EchantillonForme("medium", s.medium); EchantillonForme("large", s.large) }
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) { EchantillonForme("extraLarge", s.extraLarge) }
            Divider()
            Text("Les composants utilisent ces formes :", style = MaterialTheme.typography.bodyMedium)
            CartePlante(plantes[0])
            Button(onClick = {}) { Text("Button (shapes.full)") }
        }
    }
}

// ---------- Composants ----------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EcranComposants() {
    Surface(Modifier.fillMaxSize()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("Composants Material 3", style = MaterialTheme.typography.titleLarge)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = {}) { Text("Button") }
                FilledTonalButton(onClick = {}) { Text("Tonal") }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(onClick = {}) { Text("Outlined") }
                TextButton(onClick = {}) { Text("Text") }
            }
            OutlinedTextField(value = "Monstera", onValueChange = {}, label = { Text("Nom de la plante") }, modifier = Modifier.fillMaxWidth())
            Row(verticalAlignment = Alignment.CenterVertically) {
                Switch(checked = true, onCheckedChange = {}); Spacer(Modifier.width(8.dp)); Text("Rappels activés")
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = true, onCheckedChange = {}); Text("Plante d'intérieur")
                Spacer(Modifier.width(8.dp))
                RadioButton(selected = true, onClick = {}); Text("Soleil")
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(selected = true, onClick = {}, label = { Text("Fleurs") }, leadingIcon = { Icon(Icons.Filled.Check, null, Modifier.size(18.dp)) })
                FilterChip(selected = false, onClick = {}, label = { Text("Légumes") })
            }
            Slider(value = 0.6f, onValueChange = {})
            LinearProgressIndicator(progress = 0.4f, modifier = Modifier.fillMaxWidth())
            ElevatedCard(Modifier.fillMaxWidth()) {
                Text("ElevatedCard : couleur surface + élévation", Modifier.padding(16.dp), style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

// ---------- Couleurs étendues ----------
@Composable
fun Badge(texte: String, fond: Color, contenu: Color) {
    Surface(color = fond, contentColor = contenu, shape = CircleShape) {
        Text(texte, Modifier.padding(horizontal = 12.dp, vertical = 6.dp), style = MaterialTheme.typography.labelLarge)
    }
}

@Composable
fun EcranEtendues() {
    val ext = JardinThemeExt.couleurs
    Surface(Modifier.fillMaxSize()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("État des plantes", style = MaterialTheme.typography.titleLarge)
            listOf(Triple("Monstera", "En forme", true), Triple("Basilic", "À arroser", false), Triple("Cactus", "En forme", true)).forEach { (n, e, ok) ->
                OutlinedCard(Modifier.fillMaxWidth()) {
                    Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text(n, Modifier.weight(1f), style = MaterialTheme.typography.titleMedium)
                        if (ok) Badge(e, ext.succes, ext.surSucces) else Badge(e, ext.alerte, ext.surAlerte)
                    }
                }
            }
        }
    }
}

// ---------- Thème imbriqué ----------
@Composable
fun EcranImbrique() {
    Surface(Modifier.fillMaxSize()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("Thème principal", style = MaterialTheme.typography.titleLarge)
            Button(onClick = {}) { Text("Bouton normal") }
            CartePlante(plantes[1])
            MaterialTheme(
                colorScheme = MaterialTheme.colorScheme.copy(
                    primary = MaterialTheme.colorScheme.tertiary,
                    onPrimary = MaterialTheme.colorScheme.onTertiary,
                    secondaryContainer = MaterialTheme.colorScheme.tertiaryContainer,
                    onSecondaryContainer = MaterialTheme.colorScheme.onTertiaryContainer,
                    surfaceVariant = MaterialTheme.colorScheme.tertiaryContainer,
                    onSurfaceVariant = MaterialTheme.colorScheme.onTertiaryContainer,
                )
            ) {
                Surface(color = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.35f), shape = MaterialTheme.shapes.large) {
                    Column(Modifier.padding(16.dp).fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text("Zone « Premium » (thème imbriqué)", style = MaterialTheme.typography.titleMedium)
                        Button(onClick = {}) { Text("Bouton premium") }
                        CartePlante(Plante("Orchidée", "Arrosage : 1 fois / 10 jours"))
                    }
                }
            }
        }
    }
}

// ---------- Erreur : couleur codée en dur ----------
@Composable
fun EcranErreur(bonnePratique: Boolean) {
    Surface(Modifier.fillMaxSize()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(if (bonnePratique) "✔ Couleurs du thème" else "✘ Couleur codée en dur",
                style = MaterialTheme.typography.titleLarge,
                color = if (bonnePratique) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error)
            if (bonnePratique) {
                Text("Ce texte utilise onSurface : il reste lisible en mode clair comme en mode sombre.", style = MaterialTheme.typography.bodyLarge)
            } else {
                Text("Ce texte utilise Color.Black : en mode sombre il devient presque invisible !", style = MaterialTheme.typography.bodyLarge, color = Color.Black)
            }
        }
    }
}

// ---------- Surface et contentColor ----------
@Composable
fun EcranSurface() {
    val c = MaterialTheme.colorScheme
    Surface(Modifier.fillMaxSize()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            listOf(
                "Surface(color = primary)" to c.primary,
                "Surface(color = secondaryContainer)" to c.secondaryContainer,
                "Surface(color = tertiary)" to c.tertiary,
                "Surface(color = errorContainer)" to c.errorContainer,
            ).forEach { (titre, couleur) ->
                Surface(color = couleur, shape = MaterialTheme.shapes.small, modifier = Modifier.fillMaxWidth()) {
                    Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.LocalFlorist, null)
                        Spacer(Modifier.width(12.dp))
                        Column {
                            Text(titre, style = MaterialTheme.typography.titleMedium)
                            Text("Texte et icône colorés automatiquement", style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
        }
    }
}
