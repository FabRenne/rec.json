package com.exemple.roomcours.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation

/**
 * Classe de relation (pas une table !) :
 * une catégorie + la liste de ses tâches.
 */
data class CategorieAvecTaches(
    @Embedded
    val categorie: Categorie,

    @Relation(
        parentColumn = "id",
        entityColumn = "categorie_id"
    )
    val taches: List<Tache>
)

/** Projection : seulement quelques colonnes, résultat d'une requête agrégée. */
data class StatistiqueCategorie(
    val nom: String,
    val total: Int,
    val terminees: Int
)
