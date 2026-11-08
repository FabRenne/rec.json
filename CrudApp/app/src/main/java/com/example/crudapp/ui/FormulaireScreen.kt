package com.example.crudapp.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

private const val FORMAT_DATE = "dd/MM/yyyy"

/**
 * Écran commun à l'ajout (id == null) et à la modification (id != null).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormulaireScreen(
    vm: CrudViewModel,
    id: Long?,
    onRetour: () -> Unit
) {
    val modeModif = id != null

    var nom by rememberSaveable { mutableStateOf("") }
    var date by rememberSaveable { mutableStateOf("") }
    var charge by rememberSaveable { mutableStateOf(!modeModif) }
    var tentative by rememberSaveable { mutableStateOf(false) }
    var afficherCalendrier by remember { mutableStateOf(false) }

    // En modification : pré-remplir les champs avec l'enregistrement existant
    LaunchedEffect(id) {
        if (id != null && !charge) {
            vm.lireParId(id)?.let {
                nom = it.nom
                date = it.date
            }
            charge = true
        }
    }

    val nomErreur = tentative && nom.isBlank()
    val dateErreur = tentative && !dateValide(date)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (modeModif) "Modifier" else "Ajouter") },
                navigationIcon = {
                    IconButton(onClick = onRetour) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Retour")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp)
        ) {
            OutlinedTextField(
                value = nom,
                onValueChange = { nom = it },
                label = { Text("Nom") },
                singleLine = true,
                isError = nomErreur,
                supportingText = { if (nomErreur) Text("Le nom est obligatoire") },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = date,
                onValueChange = { date = it },
                label = { Text("Date") },
                placeholder = { Text("jj/mm/aaaa") },
                singleLine = true,
                isError = dateErreur,
                supportingText = { if (dateErreur) Text("Format attendu : jj/mm/aaaa") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                trailingIcon = {
                    IconButton(onClick = { afficherCalendrier = true }) {
                        Icon(Icons.Default.DateRange, contentDescription = "Choisir une date")
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = {
                    tentative = true
                    if (nom.isNotBlank() && dateValide(date)) {
                        if (id == null) vm.ajouter(nom.trim(), date.trim(), onRetour)
                        else vm.modifier(id, nom.trim(), date.trim(), onRetour)
                    }
                },
                enabled = charge,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text(if (modeModif) "Enregistrer les modifications" else "Ajouter")
            }
        }
    }

    if (afficherCalendrier) {
        val etat = rememberDatePickerState(initialSelectedDateMillis = parserDate(date))
        DatePickerDialog(
            onDismissRequest = { afficherCalendrier = false },
            confirmButton = {
                TextButton(onClick = {
                    etat.selectedDateMillis?.let { date = formaterDate(it) }
                    afficherCalendrier = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { afficherCalendrier = false }) { Text("Annuler") }
            }
        ) {
            DatePicker(state = etat)
        }
    }
}

// Le DatePicker travaille en UTC : on formate/parse en UTC pour éviter les décalages d'un jour.
private fun formateur() = SimpleDateFormat(FORMAT_DATE, Locale.FRANCE).apply {
    isLenient = false
    timeZone = TimeZone.getTimeZone("UTC")
}

private fun formaterDate(millis: Long): String = formateur().format(Date(millis))

private fun parserDate(texte: String): Long? =
    try {
        if (texte.trim().length == FORMAT_DATE.length) formateur().parse(texte.trim())?.time else null
    } catch (e: Exception) {
        null
    }

private fun dateValide(texte: String): Boolean = parserDate(texte) != null
