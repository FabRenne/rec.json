package com.exemple.roomcours.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.exemple.roomcours.TacheApplication
import com.exemple.roomcours.data.local.entity.Categorie
import com.exemple.roomcours.data.local.entity.Priorite
import com.exemple.roomcours.data.local.entity.Tache
import com.exemple.roomcours.data.repository.TacheRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/** État complet de l'écran, exposé à Compose. */
data class TachesUiState(
    val taches: List<Tache> = emptyList(),
    val categories: List<Categorie> = emptyList(),
    val recherche: String = "",
    val restantes: Int = 0,
    val chargement: Boolean = true
)

@OptIn(ExperimentalCoroutinesApi::class)
class TacheViewModel(private val repository: TacheRepository) : ViewModel() {

    private val recherche = MutableStateFlow("")

    // Chaque fois que "recherche" change, on relance la requête Room
    private val tachesFiltrees = recherche.flatMapLatest { texte ->
        if (texte.isBlank()) repository.taches else repository.rechercher(texte)
    }

    val uiState: StateFlow<TachesUiState> = combine(
        tachesFiltrees,
        repository.categories,
        repository.nombreRestantes,
        recherche
    ) { taches, categories, restantes, texte ->
        TachesUiState(taches, categories, texte, restantes, chargement = false)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000), // survit aux rotations
        initialValue = TachesUiState()
    )

    fun onRechercheChange(texte: String) { recherche.value = texte }

    fun ajouter(titre: String, priorite: Priorite, categorieId: Long?) {
        if (titre.isBlank()) return
        viewModelScope.launch {
            repository.ajouter(
                Tache(titre = titre.trim(), priorite = priorite, categorieId = categorieId)
            )
        }
    }

    fun basculer(tache: Tache) = viewModelScope.launch { repository.basculer(tache) }
    fun supprimer(tache: Tache) = viewModelScope.launch { repository.supprimer(tache) }
    fun nettoyer() = viewModelScope.launch { repository.nettoyer() }
    fun ajouterCategorie(nom: String) = viewModelScope.launch {
        if (nom.isNotBlank()) repository.ajouterCategorie(nom)
    }

    companion object {
        /** Fabrique : permet de passer le repository au constructeur. */
        val Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as TacheApplication
                TacheViewModel(app.repository)
            }
        }
    }
}
