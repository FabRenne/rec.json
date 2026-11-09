package com.exemple.mesrecettes.exemple5

import kotlinx.serialization.Serializable

// Chapitre 7 : renvoyer un résultat à l'écran précédent

@Serializable
object Liste

@Serializable
data class Detail(val id: Int)

@Serializable
data class Noter(val id: Int)
