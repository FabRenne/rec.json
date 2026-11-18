package com.exemple.roomcours.data.repository

import com.exemple.roomcours.data.local.dao.CategorieDao
import com.exemple.roomcours.data.local.dao.TacheDao
import com.exemple.roomcours.data.local.entity.Categorie
import com.exemple.roomcours.data.local.entity.CategorieAvecTaches
import com.exemple.roomcours.data.local.entity.Tache
import kotlinx.coroutines.flow.Flow

/**
 * Le Repository isole la source de données.
 * Le ViewModel ne sait pas que les données viennent de Room
 * (on pourrait ajouter une API réseau sans toucher à l'UI).
 */
class TacheRepository(
    private val tacheDao: TacheDao,
    private val categorieDao: CategorieDao
) {
    val taches: Flow<List<Tache>> = tacheDao.observerToutes()
    val categories: Flow<List<Categorie>> = categorieDao.observerToutes()
    val categoriesAvecTaches: Flow<List<CategorieAvecTaches>> = categorieDao.observerAvecTaches()
    val nombreRestantes: Flow<Int> = tacheDao.compterRestantes()

    fun rechercher(texte: String): Flow<List<Tache>> = tacheDao.rechercher(texte)

    suspend fun ajouter(tache: Tache): Long = tacheDao.inserer(tache)
    suspend fun basculer(tache: Tache) = tacheDao.changerEtat(tache.id, !tache.estTerminee)
    suspend fun supprimer(tache: Tache) = tacheDao.supprimer(tache)
    suspend fun nettoyer(): Int = tacheDao.supprimerTerminees()

    suspend fun ajouterCategorie(nom: String): Long =
        categorieDao.inserer(Categorie(nom = nom.trim()))
}
