package com.example.crudapp.data

import android.content.ContentValues
import android.content.Context
import android.database.DatabaseUtils
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * Accès direct à SQLite (sans Room) : création de la table et opérations CRUD.
 */
class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE $TABLE (
                $COL_ID   INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_NOM  TEXT NOT NULL,
                $COL_DATE TEXT NOT NULL
            )
            """.trimIndent()
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE")
        onCreate(db)
    }

    // ---------- CREATE ----------
    fun ajouter(e: Enregistrement): Long =
        writableDatabase.insert(TABLE, null, toValues(e))

    // ---------- READ ----------
    fun compter(): Long = DatabaseUtils.queryNumEntries(readableDatabase, TABLE)

    fun lireTous(): List<Enregistrement> {
        val liste = mutableListOf<Enregistrement>()
        readableDatabase.query(
            TABLE, arrayOf(COL_ID, COL_NOM, COL_DATE),
            null, null, null, null, "$COL_NOM COLLATE NOCASE ASC"
        ).use { c ->
            while (c.moveToNext()) {
                liste += Enregistrement(
                    id = c.getLong(c.getColumnIndexOrThrow(COL_ID)),
                    nom = c.getString(c.getColumnIndexOrThrow(COL_NOM)),
                    date = c.getString(c.getColumnIndexOrThrow(COL_DATE))
                )
            }
        }
        return liste
    }

    fun lireParId(id: Long): Enregistrement? =
        readableDatabase.query(
            TABLE, arrayOf(COL_ID, COL_NOM, COL_DATE),
            "$COL_ID = ?", arrayOf(id.toString()), null, null, null
        ).use { c ->
            if (c.moveToFirst()) Enregistrement(
                id = c.getLong(c.getColumnIndexOrThrow(COL_ID)),
                nom = c.getString(c.getColumnIndexOrThrow(COL_NOM)),
                date = c.getString(c.getColumnIndexOrThrow(COL_DATE))
            ) else null
        }

    // ---------- UPDATE ----------
    fun modifier(e: Enregistrement): Int =
        writableDatabase.update(TABLE, toValues(e), "$COL_ID = ?", arrayOf(e.id.toString()))

    // ---------- DELETE ----------
    fun supprimer(id: Long): Int =
        writableDatabase.delete(TABLE, "$COL_ID = ?", arrayOf(id.toString()))

    private fun toValues(e: Enregistrement) = ContentValues().apply {
        put(COL_NOM, e.nom)
        put(COL_DATE, e.date)
    }

    companion object {
        private const val DB_NAME = "crud.db"
        private const val DB_VERSION = 1
        const val TABLE = "enregistrements"
        const val COL_ID = "id"
        const val COL_NOM = "nom"
        const val COL_DATE = "date"
    }
}
