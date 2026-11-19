package com.example.moutures2

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

/** Texte affiché quand une valeur est absente (tiret long, pas le « - » du clavier). */
const val VIDE = "—"

private val VertOk = Color(0xFF2E7D32)

// ─────────────────────────────────────────────────────────────
// 1. L'écran relié au ViewModel
// ─────────────────────────────────────────────────────────────

@Composable
fun MouturesScreen(viewModel: MoutureViewModel = viewModel()) {
    val etat by viewModel.uiState.collectAsStateWithLifecycle()
    MouturesContent(etat = etat, onMoutureClick = viewModel::onMoutureClick)
}

// ─────────────────────────────────────────────────────────────
// 2. Le contenu : ne reçoit que des données (facile à prévisualiser)
// ─────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MouturesContent(etat: MouturesUiState, onMoutureClick: (Mouture) -> Unit) {
    val listState = rememberLazyListState()

    // À chaque changement de sélection, on remonte en haut pour montrer la carte.
    LaunchedEffect(etat.selection) {
        if (etat.selection != null) listState.animateScrollToItem(0)
    }

    Scaffold(topBar = { TopAppBar(title = { Text("Moutures") }) }) { padding ->
        Box(Modifier.fillMaxSize().padding(padding)) {
            when {
                etat.chargement -> CircularProgressIndicator(Modifier.align(Alignment.Center))

                etat.erreur != null -> Text(
                    text = etat.erreur,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.Center).padding(16.dp),
                )

                else -> LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    // 1) la carte de détail, en tête de liste
                    etat.selection?.let { mouture ->
                        item(key = "detail") { DetailMouture(mouture) }
                    }
                    // 2) une ligne par mouture
                    items(etat.moutures, key = { it.nom }) { mouture ->
                        LigneMouture(
                            mouture = mouture,
                            selectionnee = mouture.nom == etat.selection?.nom,
                            onClick = { onMoutureClick(mouture) },
                        )
                    }
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────
// 3. Une ligne de la liste
// ─────────────────────────────────────────────────────────────

@Composable
private fun LigneMouture(mouture: Mouture, selectionnee: Boolean, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = if (selectionnee) MaterialTheme.colorScheme.primaryContainer
                             else MaterialTheme.colorScheme.surfaceVariant,
        ),
    ) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(
                mouture.nom,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(1f),
            )
            Text(
                "SM ${mouture.sm.vb}/${mouture.sm.vd} · AM ${mouture.am.vb}/${mouture.am.vd}",
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────
// 4. La carte de détail
// ─────────────────────────────────────────────────────────────

@Composable
private fun DetailMouture(mouture: Mouture) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
        ),
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(
                mouture.nom,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
            )
            TableauDosage(sm = mouture.sm, am = mouture.am)
            HorizontalDivider(Modifier.padding(vertical = 4.dp))
            val info = mouture.info
            LigneInfo("Gruau", info.gruau ?: VIDE)
            LigneInfo("Améliorant", info.ameliorant ?: VIDE)
            LigneInfo("Mignon", info.mignon ?: VIDE)
            IconesRemoulage(titre = "Remoulage", valeurs = info.remoulage)
            LigneInfo("Dév. sûreté", if (info.devSurete) "Oui" else "Non")
        }
    }
}

@Composable
private fun LigneInfo(libelle: String, valeur: String) {
    Row(Modifier.fillMaxWidth()) {
        Text(libelle, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(1f))
        Text(valeur, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
    }
}

// ─────────────────────────────────────────────────────────────
// 5. Le tableau des vitesses
// ─────────────────────────────────────────────────────────────

@Composable
private fun TableauDosage(sm: Dosage, am: Dosage) {
    val forme = RoundedCornerShape(8.dp)
    Column(
        Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface, forme)   // fond AVANT la bordure,
            .border(1.dp, Color.Gray, forme),                      // même forme pour les deux
    ) {
        LigneTableau("", "Sans Meule", "Avec Meule", entete = true)
        HorizontalDivider(thickness = 1.dp, color = Color.Gray)
        LigneTableau("Vit Blé", sm.vb.toString(), am.vb.toString())
        HorizontalDivider(thickness = 1.dp, color = Color.Gray)
        LigneTableau("Vit Doseur", sm.vd.toString(), am.vd.toString())
    }
}

@Composable
private fun LigneTableau(libelle: String, a: String, b: String, entete: Boolean = false) {
    // IntrinsicSize.Min : la Row prend la hauteur de son plus grand enfant,
    // ce qui rend les VerticalDivider visibles.
    Row(Modifier.fillMaxWidth().height(IntrinsicSize.Min)) {
        CaseTableau(libelle, Modifier.weight(1.2f), gras = true, alignement = TextAlign.Start)
        VerticalDivider(thickness = 1.dp, color = Color.Gray)
        CaseTableau(a, Modifier.weight(1f), gras = entete)
        VerticalDivider(thickness = 1.dp, color = Color.Gray)
        CaseTableau(b, Modifier.weight(1f), gras = entete)
    }
}

@Composable
private fun CaseTableau(
    texte: String,
    modifier: Modifier = Modifier,
    gras: Boolean = false,
    alignement: TextAlign = TextAlign.Center,
) {
    Text(
        texte,
        modifier = modifier.padding(horizontal = 8.dp, vertical = 6.dp),
        fontWeight = if (gras) FontWeight.SemiBold else FontWeight.Normal,
        textAlign = alignement,
    )
}

// ─────────────────────────────────────────────────────────────
// 6. Les icônes du remoulage
// ─────────────────────────────────────────────────────────────

@Composable
fun IconesRemoulage(titre: String, valeurs: List<Boolean>, modifier: Modifier = Modifier) {
    Column(modifier, verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(titre, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            // Toujours 3 icônes, même si le JSON contient moins de valeurs.
            List(3) { i -> valeurs.getOrElse(i) { false } }.forEachIndexed { i, actif ->
                Icon(
                    imageVector = if (actif) Icons.Filled.CheckCircle else Icons.Filled.Close,
                    contentDescription = "Remoulage ${i + 1} : ${if (actif) "oui" else "non"}",
                    tint = if (actif) VertOk else MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(28.dp),
                )
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────
// 7. Aperçu dans Android Studio (sans lancer l'application)
// ─────────────────────────────────────────────────────────────

@Preview(showBackground = true, heightDp = 720)
@Composable
private fun ApercuMoutures() {
    val tradition = Mouture(
        nom = "Tradition",
        sm = Dosage(vb = 21, vd = 72),
        am = Dosage(vb = 19, vd = 67),
        info = InfoMouture("doseur à 18", "Floricreme", "doseur à 18", listOf(true, false, false), false),
    )
    val t55 = Mouture(
        nom = "t55",
        sm = Dosage(21, 0),
        am = Dosage(0, 0),
        info = InfoMouture(null, null, null, listOf(false, false, false), false),
    )
    MouturesTheme(darkTheme = false) {
        MouturesContent(
            etat = MouturesUiState(
                chargement = false,
                moutures = listOf(t55, tradition),
                selection = tradition,
            ),
            onMoutureClick = {},
        )
    }
}
