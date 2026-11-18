package com.exemple.roomcours.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Table "categories" : une catégorie regroupe plusieurs tâches (relation 1-N).
 */
@Entity(
    tableName = "categories",
    indices = [Index(value = ["nom"], unique = true)] // pas deux catégories du même nom
)
data class Categorie(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "nom")
    val nom: String,

    @ColumnInfo(name = "couleur")
    val couleur: Long = 0xFF6750A4 // couleur ARGB stockée en entier
)
