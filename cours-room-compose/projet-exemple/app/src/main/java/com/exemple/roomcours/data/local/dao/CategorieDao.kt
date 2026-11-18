package com.exemple.roomcours.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.exemple.roomcours.data.local.entity.Categorie
import com.exemple.roomcours.data.local.entity.CategorieAvecTaches
import com.exemple.roomcours.data.local.entity.StatistiqueCategorie
import kotlinx.coroutines.flow.Flow

@Dao
interface CategorieDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun inserer(categorie: Categorie): Long   // -1 si le nom existe déjà

    @Delete
    suspend fun supprimer(categorie: Categorie)        // CASCADE : supprime ses tâches

    @Query("SELECT * FROM categories ORDER BY nom")
    fun observerToutes(): Flow<List<Categorie>>

    // @Transaction obligatoire : Room exécute 2 requêtes (catégories puis tâches)
    @Transaction
    @Query("SELECT * FROM categories ORDER BY nom")
    fun observerAvecTaches(): Flow<List<CategorieAvecTaches>>

    // Requête avec jointure et agrégation -> projection dans une simple data class
    @Query(
        """
        SELECT c.nom AS nom,
               COUNT(t.id) AS total,
               COALESCE(SUM(t.est_terminee), 0) AS terminees
        FROM categories c
        LEFT JOIN taches t ON t.categorie_id = c.id
        GROUP BY c.id
        ORDER BY c.nom
        """
    )
    fun statistiques(): Flow<List<StatistiqueCategorie>>
}
