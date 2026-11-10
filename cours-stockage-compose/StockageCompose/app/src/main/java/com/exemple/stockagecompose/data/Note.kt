package com.exemple.stockagecompose.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Une "Entity" = une table de la base de données.
 * Chaque propriété devient une colonne.
 */
@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val titre: String,
    val dateCreation: Long = System.currentTimeMillis()
)
