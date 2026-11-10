package com.exemple.stockagecompose.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * La base de données : elle liste les entités et donne accès aux DAO.
 */
@Database(entities = [Note::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun noteDao(): NoteDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        // On crée la base une seule fois (singleton)
        fun get(context: Context): AppDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "mes_notes.db"
                ).build().also { instance = it }
            }
    }
}
