package com.exemple.roomcours.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDate

enum class Priorite { BASSE, NORMALE, HAUTE }

/**
 * Table "taches".
 * - clé primaire auto-générée
 * - clé étrangère vers "categories" (suppression en cascade)
 * - LocalDate et Priorite sont convertis grâce aux TypeConverters
 */
@Entity(
    tableName = "taches",
    foreignKeys = [
        ForeignKey(
            entity = Categorie::class,
            parentColumns = ["id"],
            childColumns = ["categorie_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("categorie_id")] // un index sur chaque clé étrangère
)
data class Tache(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val titre: String,

    val description: String = "",

    @ColumnInfo(name = "est_terminee", defaultValue = "0")
    val estTerminee: Boolean = false,

    val priorite: Priorite = Priorite.NORMALE,

    @ColumnInfo(name = "date_echeance")
    val dateEcheance: LocalDate? = null,

    @ColumnInfo(name = "categorie_id")
    val categorieId: Long? = null,

    // Ajoutée en version 2 de la base (voir MIGRATION_1_2)
    @ColumnInfo(name = "cree_le", defaultValue = "0")
    val creeLe: Long = System.currentTimeMillis()
) {
    // Propriété calculée : jamais stockée en base
    @Ignore
    val estEnRetard: Boolean =
        !estTerminee && dateEcheance != null && dateEcheance.isBefore(LocalDate.now())
}
