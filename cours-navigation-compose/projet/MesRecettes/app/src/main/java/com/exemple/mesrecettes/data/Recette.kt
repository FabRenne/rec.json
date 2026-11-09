package com.exemple.mesrecettes.data

/** Une recette de notre application d'exemple. */
data class Recette(
    val id: Int,
    val nom: String,
    val emoji: String,
    val duree: Int,              // en minutes
    val difficulte: String,
    val description: String,
    val auteur: String,
)

/** Les recettes affichées dans tous les exemples. */
val recettes = listOf(
    Recette(1, "Crêpes", "🥞", 20, "Facile", "Mélangez la farine, les œufs et le lait, puis faites cuire à la poêle.", "Léa"),
    Recette(2, "Pizza maison", "🍕", 45, "Moyen", "Étalez la pâte, ajoutez la sauce tomate et la garniture, puis enfournez.", "Hugo"),
    Recette(3, "Salade fraîcheur", "🥗", 10, "Facile", "Coupez les légumes, ajoutez une vinaigrette et mélangez.", "Inès"),
    Recette(4, "Soupe de légumes", "🍲", 35, "Facile", "Faites cuire les légumes dans l'eau puis mixez le tout.", "Léa"),
    Recette(5, "Gâteau au chocolat", "🍰", 50, "Moyen", "Faites fondre le chocolat, ajoutez le beurre, le sucre, les œufs et la farine.", "Hugo"),
    Recette(6, "Tacos", "🌮", 25, "Facile", "Garnissez les tortillas de viande, de salade et de sauce.", "Inès"),
)

/** Retrouve une recette à partir de son identifiant. */
fun recetteParId(id: Int): Recette = recettes.first { it.id == id }
