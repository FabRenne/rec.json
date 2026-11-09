package com.exemple.mesrecettes.exemple3

import kotlinx.serialization.Serializable

// Chapitre 5 : gérer la pile d'écrans

@Serializable
object Connexion

@Serializable
data class AccueilConnecte(val prenom: String)
