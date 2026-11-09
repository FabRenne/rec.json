package com.exemple.mesrecettes.exemple4

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.serialization.Serializable

// Chapitre 6 : une destination par onglet

@Serializable
object Recettes

@Serializable
object Favoris

@Serializable
object Profil

/** Décrit un onglet de la barre du bas. */
data class Onglet<T : Any>(val titre: String, val icone: ImageVector, val route: T)

val onglets = listOf(
    Onglet("Recettes", Icons.Filled.Home, Recettes),
    Onglet("Favoris", Icons.Filled.Favorite, Favoris),
    Onglet("Profil", Icons.Filled.Person, Profil),
)
