package com.example.moutures2

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/** Tout ce que l'écran doit afficher à un instant donné. */
data class MouturesUiState(
    val chargement: Boolean = true,
    val moutures: List<Mouture> = emptyList(),
    val selection: Mouture? = null,
    val erreur: String? = null,
)

/**
 * Garde l'état de l'écran et le fait évoluer.
 * Il survit à la rotation : le JSON n'est lu qu'une seule fois.
 */
class MoutureViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = MoutureRepository(application)

    // Privé et modifiable : seul le ViewModel peut changer l'état.
    private val _uiState = MutableStateFlow(MouturesUiState())

    // Public et en lecture seule : c'est ce que l'écran observe.
    val uiState: StateFlow<MouturesUiState> = _uiState.asStateFlow()

    init {
        // Lecture du JSON au démarrage, puis sélection du 1er élément.
        viewModelScope.launch {
            _uiState.value = try {
                val moutures = repository.chargerMoutures()
                MouturesUiState(
                    chargement = false,
                    moutures = moutures,
                    selection = moutures.firstOrNull(),
                )
            } catch (e: Exception) {
                MouturesUiState(
                    chargement = false,
                    erreur = "Impossible de lire rec.json : ${e.message}",
                )
            }
        }
    }

    /** Appelée par l'écran quand on touche une ligne. */
    fun onMoutureClick(mouture: Mouture) {
        _uiState.update { it.copy(selection = mouture) }
        // Variante « bascule » (exercice 5) :
        // _uiState.update {
        //     it.copy(selection = if (it.selection?.nom == mouture.nom) null else mouture)
        // }
    }
}
