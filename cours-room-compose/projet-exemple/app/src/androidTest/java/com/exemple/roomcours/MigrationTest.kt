package com.exemple.roomcours

import androidx.room.testing.MigrationTestHelper
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.exemple.roomcours.data.local.AppDatabase
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Vérifie que MIGRATION_1_2 produit bien le schéma attendu (fichier schemas/2.json).
 * Nécessite les schémas exportés 1.json et 2.json.
 */
@RunWith(AndroidJUnit4::class)
class MigrationTest {

    @get:Rule
    val helper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        AppDatabase::class.java
    )

    @Test
    fun migration1Vers2() {
        helper.createDatabase("test-migration", 1).apply {
            execSQL("INSERT INTO taches (titre, description, est_terminee, priorite) VALUES ('Ancienne', '', 0, 'NORMALE')")
            close()
        }
        helper.runMigrationsAndValidate("test-migration", 2, true, AppDatabase.MIGRATION_1_2)
    }
}
