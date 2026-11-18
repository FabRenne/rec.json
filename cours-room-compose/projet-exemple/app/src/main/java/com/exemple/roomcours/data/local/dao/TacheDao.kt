package com.exemple.roomcours.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import androidx.room.Upsert
import com.exemple.roomcours.data.local.entity.Tache
import kotlinx.coroutines.flow.Flow

/**
 * DAO = Data Access Object : l'interface qui décrit les opérations SQL.
 * Room génère l'implémentation à la compilation (TacheDao_Impl).
 */
@Dao
interface TacheDao {

    // ---------- Écriture : fonctions suspend (hors thread principal) ----------

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun inserer(tache: Tache): Long          // renvoie l'id généré

    @Insert
    suspend fun insererTout(taches: List<Tache>): List<Long>

    @Update
    suspend fun modifier(tache: Tache): Int           // nb de lignes modifiées

    @Upsert                                           // insert OU update
    suspend fun enregistrer(tache: Tache)

    @Delete
    suspend fun supprimer(tache: Tache): Int

    @Query("UPDATE taches SET est_terminee = :terminee WHERE id = :id")
    suspend fun changerEtat(id: Long, terminee: Boolean)

    @Query("DELETE FROM taches WHERE est_terminee = 1")
    suspend fun supprimerTerminees(): Int

    // ---------- Lecture réactive : Flow (mis à jour automatiquement) ----------

    @Query("SELECT * FROM taches ORDER BY est_terminee ASC, priorite DESC, cree_le DESC")
    fun observerToutes(): Flow<List<Tache>>

    @Query("SELECT * FROM taches WHERE titre LIKE '%' || :texte || '%' ORDER BY cree_le DESC")
    fun rechercher(texte: String): Flow<List<Tache>>

    @Query("SELECT * FROM taches WHERE categorie_id = :categorieId")
    fun observerParCategorie(categorieId: Long): Flow<List<Tache>>

    @Query("SELECT COUNT(*) FROM taches WHERE est_terminee = 0")
    fun compterRestantes(): Flow<Int>

    // ---------- Lecture ponctuelle ----------

    @Query("SELECT * FROM taches WHERE id = :id")
    suspend fun trouverParId(id: Long): Tache?

    // ---------- Transaction : plusieurs opérations atomiques ----------

    @Transaction
    suspend fun remplacerTout(taches: List<Tache>) {
        toutSupprimer()
        insererTout(taches)
    }

    @Query("DELETE FROM taches")
    suspend fun toutSupprimer()
}
