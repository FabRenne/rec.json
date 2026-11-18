package com.exemple.roomcours.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.exemple.roomcours.data.local.converter.Converters
import com.exemple.roomcours.data.local.dao.CategorieDao
import com.exemple.roomcours.data.local.dao.TacheDao
import com.exemple.roomcours.data.local.entity.Categorie
import com.exemple.roomcours.data.local.entity.Tache

/**
 * Point d'entrée de la base.
 * - entities : liste des tables
 * - version  : à incrémenter à chaque changement de schéma
 * - exportSchema : génère app/schemas/<version>.json
 */
@Database(
    entities = [Tache::class, Categorie::class],
    version = 2,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun tacheDao(): TacheDao
    abstract fun categorieDao(): CategorieDao

    companion object {
        private const val NOM_BASE = "taches.db"

        // @Volatile : la valeur est toujours lue depuis la mémoire principale
        @Volatile
        private var INSTANCE: AppDatabase? = null

        /** Singleton : une seule instance de la base pour toute l'application. */
        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    NOM_BASE
                )
                    .addMigrations(MIGRATION_1_2)
                    // .fallbackToDestructiveMigration(dropAllTables = true) // à éviter en prod !
                    .build()
                    .also { INSTANCE = it }
            }

        /** v1 -> v2 : ajout de la colonne "cree_le" dans "taches". */
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "ALTER TABLE taches ADD COLUMN cree_le INTEGER NOT NULL DEFAULT 0"
                )
            }
        }
    }
}
