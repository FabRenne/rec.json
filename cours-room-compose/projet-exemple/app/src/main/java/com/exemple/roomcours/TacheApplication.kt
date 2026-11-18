package com.exemple.roomcours

import android.app.Application
import com.exemple.roomcours.data.local.AppDatabase
import com.exemple.roomcours.data.repository.TacheRepository

/**
 * Conteneur de dépendances minimal (sans Hilt/Koin) :
 * la base et le repository sont créés paresseusement, une seule fois.
 */
class TacheApplication : Application() {
    val database: AppDatabase by lazy { AppDatabase.getInstance(this) }
    val repository: TacheRepository by lazy {
        TacheRepository(database.tacheDao(), database.categorieDao())
    }
}
