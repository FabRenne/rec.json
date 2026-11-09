package com.exemple.mesrecettes.exemple2

import kotlinx.serialization.Serializable

// Chapitre 4 : une destination AVEC argument = une data class

@Serializable
object ListeRecettes

@Serializable
data class Detail(val id: Int)

@Serializable
data class Auteur(val nom: String)      // exercice 2
