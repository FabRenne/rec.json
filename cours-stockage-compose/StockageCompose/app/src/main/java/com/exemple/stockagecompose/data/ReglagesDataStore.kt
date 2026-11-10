package com.exemple.stockagecompose.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Un seul DataStore pour toute l'application (déclaré au niveau du fichier)
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "reglages")

/**
 * Petite classe qui regroupe la lecture / écriture dans DataStore.
 */
class ReglagesDataStore(private val context: Context) {

    companion object {
        val CLE_PRENOM = stringPreferencesKey("prenom")
        val CLE_COMPTEUR = intPreferencesKey("compteur")
    }

    // Lecture : un Flow qui émet une nouvelle valeur à chaque modification
    val prenom: Flow<String> = context.dataStore.data.map { it[CLE_PRENOM] ?: "" }
    val compteur: Flow<Int> = context.dataStore.data.map { it[CLE_COMPTEUR] ?: 0 }

    // Écriture : fonctions "suspend" à appeler dans une coroutine
    suspend fun enregistrerPrenom(valeur: String) {
        context.dataStore.edit { it[CLE_PRENOM] = valeur }
    }

    suspend fun incrementer() {
        context.dataStore.edit { it[CLE_COMPTEUR] = (it[CLE_COMPTEUR] ?: 0) + 1 }
    }

    suspend fun remettreAZero() {
        context.dataStore.edit { it[CLE_COMPTEUR] = 0 }
    }
}
