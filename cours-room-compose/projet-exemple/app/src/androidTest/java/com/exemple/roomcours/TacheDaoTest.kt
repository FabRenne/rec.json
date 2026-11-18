package com.exemple.roomcours

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.exemple.roomcours.data.local.AppDatabase
import com.exemple.roomcours.data.local.dao.CategorieDao
import com.exemple.roomcours.data.local.dao.TacheDao
import com.exemple.roomcours.data.local.entity.Categorie
import com.exemple.roomcours.data.local.entity.Tache
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TacheDaoTest {

    private lateinit var db: AppDatabase
    private lateinit var tacheDao: TacheDao
    private lateinit var categorieDao: CategorieDao

    @Before
    fun creerBase() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        // Base en mémoire : effacée à la fin du test
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries() // uniquement pour les tests !
            .build()
        tacheDao = db.tacheDao()
        categorieDao = db.categorieDao()
    }

    @After
    fun fermerBase() = db.close()

    @Test
    fun insererPuisLire() = runTest {
        val id = tacheDao.inserer(Tache(titre = "Réviser Room"))
        val lue = tacheDao.trouverParId(id)
        assertEquals("Réviser Room", lue?.titre)
    }

    @Test
    fun flowEmetApresInsertion() = runTest {
        tacheDao.inserer(Tache(titre = "A"))
        tacheDao.inserer(Tache(titre = "B"))
        val liste = tacheDao.observerToutes().first()
        assertEquals(2, liste.size)
    }

    @Test
    fun changerEtatEtNettoyer() = runTest {
        val id = tacheDao.inserer(Tache(titre = "Faire les courses"))
        tacheDao.changerEtat(id, true)
        assertEquals(1, tacheDao.supprimerTerminees())
        assertNull(tacheDao.trouverParId(id))
    }

    @Test
    fun suppressionEnCascade() = runTest {
        val catId = categorieDao.inserer(Categorie(nom = "Travail"))
        tacheDao.inserer(Tache(titre = "Rapport", categorieId = catId))
        categorieDao.supprimer(Categorie(id = catId, nom = "Travail"))
        assertTrue(tacheDao.observerToutes().first().isEmpty())
    }

    @Test
    fun relationCategorieAvecTaches() = runTest {
        val catId = categorieDao.inserer(Categorie(nom = "Maison"))
        tacheDao.insererTout(
            listOf(Tache(titre = "Ménage", categorieId = catId), Tache(titre = "Linge", categorieId = catId))
        )
        val resultat = categorieDao.observerAvecTaches().first()
        assertEquals(2, resultat.first().taches.size)
    }
}
