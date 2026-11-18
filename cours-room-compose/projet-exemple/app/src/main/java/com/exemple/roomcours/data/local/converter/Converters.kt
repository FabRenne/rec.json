package com.exemple.roomcours.data.local.converter

import androidx.room.TypeConverter
import com.exemple.roomcours.data.local.entity.Priorite
import java.time.LocalDate

/**
 * SQLite ne connaît que INTEGER, REAL, TEXT et BLOB.
 * Les TypeConverters expliquent à Room comment stocker les autres types.
 */
class Converters {

    // LocalDate <-> Long (nombre de jours depuis 1970-01-01)
    @TypeConverter
    fun fromLocalDate(date: LocalDate?): Long? = date?.toEpochDay()

    @TypeConverter
    fun toLocalDate(jours: Long?): LocalDate? = jours?.let(LocalDate::ofEpochDay)

    // Priorite <-> String
    // (Room sait déjà convertir les enum, ceci est un exemple explicite)
    @TypeConverter
    fun fromPriorite(p: Priorite): String = p.name

    @TypeConverter
    fun toPriorite(valeur: String): Priorite = Priorite.valueOf(valeur)
}
