package com.exemple.mesfruits.exemple6

import kotlinx.serialization.Serializable

/* Chapitre 8 : les destinations (« routes ») de Navigation Compose. */

@Serializable
object Accueil                         // écran sans paramètre

@Serializable
data class Detail(val nom: String)     // écran avec un paramètre
