package com.fabrenne.moutures.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fabrenne.moutures.MoutureViewModel
import com.fabrenne.moutures.MouturesUiState
import com.fabrenne.moutures.data.Dosage
import com.fabrenne.moutures.data.InfoMouture
import com.fabrenne.moutures.data.Mouture
import com.fabrenne.moutures.ui.theme.MouturesTheme

@Composable
fun MouturesScreen(viewModel: MoutureViewModel = viewModel()) {
    val etat by viewModel.uiState.collectAsStateWithLifecycle()
    MouturesContent(etat = etat, onMoutureClick = viewModel::onMoutureClick)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MouturesContent(
    etat: MouturesUiState,
    onMoutureClick: (Mouture) -> Unit,
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Moutures") }) },
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            when {
                etat.chargement -> CircularProgressIndicator(Modifier.align(Alignment.Center))
                etat.erreur != null -> Text(
                    text = etat.erreur,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp),
                )
                else -> Column(Modifier.fillMaxSize()) {
                    // Zone d'informations, au-dessus de la LazyColumn, sur le même écran.
                    AnimatedVisibility(
                        visible = etat.selection != null,
                        enter = expandVertically() + fadeIn(),
                        exit = shrinkVertically() + fadeOut(),
                    ) {
                        AnimatedContent(
                            targetState = etat.selection,
                            transitionSpec = { fadeIn() togetherWith fadeOut() },
                            label = "detail",
                        ) { mouture ->
                            if (mouture != null) DetailMouture(mouture)
                        }
                    }

                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
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
}

@Composable
private fun LigneMouture(
    mouture: Mouture,
    selectionnee: Boolean,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = if (selectionnee) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.surfaceVariant
            },
        ),
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = mouture.nom,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(1f),
            )
            Text(
                text = "SM ${mouture.sm.vb}/${mouture.sm.vd} · AM ${mouture.am.vb}/${mouture.am.vd}",
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}

@Composable
private fun DetailMouture(mouture: Mouture) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(
                text = mouture.nom,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
            )

            Row(Modifier.fillMaxWidth()) {
                BlocDosage("SM", mouture.sm, Modifier.weight(1f))
                Spacer(Modifier.width(16.dp))
                BlocDosage("AM", mouture.am, Modifier.weight(1f))
            }

            HorizontalDivider(Modifier.padding(vertical = 4.dp))

            val info = mouture.info
            LigneInfo("Gruau", info.gruau ?: "—")
            LigneInfo("Améliorant", info.ameliorant ?: "—")
            LigneInfo("Mignon", info.mignon ?: "—")
            LigneInfo(
                "Remoulage",
                if (info.remoulage.isEmpty()) "—"
                else info.remoulage.joinToString("  ") { if (it) "✔" else "✘" },
            )
            LigneInfo("Dév. sûreté", if (info.devSurete) "Oui" else "Non")
        }
    }
}

@Composable
private fun BlocDosage(titre: String, dosage: Dosage, modifier: Modifier = Modifier) {
    Column(modifier) {
        Text(titre, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
        LigneInfo("VB", dosage.vb.toString())
        LigneInfo("VD", dosage.vd.toString())
    }
}

@Composable
private fun LigneInfo(libelle: String, valeur: String) {
    Row(Modifier.fillMaxWidth()) {
        Text(
            text = libelle,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f),
        )
        Text(
            text = valeur,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MouturesPreview() {
    val tradition = Mouture(
        nom = "Tradition",
        sm = Dosage(21, 72),
        am = Dosage(19, 67),
        info = InfoMouture("doseur à 18", "Floricreme", "doseur à 18", listOf(true, false, false), false),
    )
    val t55 = Mouture(
        nom = "t55",
        sm = Dosage(21, 0),
        am = Dosage(0, 0),
        info = InfoMouture(null, null, null, listOf(false, false, false), false),
    )
    MouturesTheme {
        MouturesContent(
            etat = MouturesUiState(chargement = false, moutures = listOf(t55, tradition), selection = tradition),
            onMoutureClick = {},
        )
    }
}
