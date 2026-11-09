package com.exemple.mesfruits.data

/** Un fruit de notre application d'exemple. */
data class Fruit(val nom: String, val emoji: String, val description: String)

/** La liste des fruits affichée dans tous les exemples. */
val fruits = listOf(
    Fruit("Pomme", "🍎", "Croquante et sucrée"),
    Fruit("Banane", "🍌", "Pleine d'énergie"),
    Fruit("Raisin", "🍇", "À picorer"),
    Fruit("Fraise", "🍓", "Le fruit de l'été"),
    Fruit("Orange", "🍊", "Pleine de vitamine C"),
    Fruit("Kiwi", "🥝", "Petit mais costaud"),
)

/** Retrouve un fruit à partir de son nom. */
fun fruitParNom(nom: String): Fruit = fruits.first { it.nom == nom }
