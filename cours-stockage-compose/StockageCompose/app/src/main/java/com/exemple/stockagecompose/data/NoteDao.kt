package com.exemple.stockagecompose.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * DAO = Data Access Object : la liste des opérations possibles sur la table.
 */
@Dao
interface NoteDao {

    @Query("SELECT * FROM notes ORDER BY dateCreation DESC")
    fun toutesLesNotes(): Flow<List<Note>>

    @Insert
    suspend fun ajouter(note: Note)

    @Delete
    suspend fun supprimer(note: Note)
}
