package com.fabrenne.moutures

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.fabrenne.moutures.data.Mouture
import com.fabrenne.moutures.data.MoutureRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class MouturesUiState(
    val chargement: Boolean = true,
    val moutures: List<Mouture> = emptyList(),
    val selection: Mouture? = null,
    val erreur: String? = null,
)

class MoutureViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = MoutureRepository(application)

    private val _uiState = MutableStateFlow(MouturesUiState())
    val uiState: StateFlow<MouturesUiState> = _uiState.asStateFlow()

    init {
        // Lecture du fichier JSON au démarrage de l'application.
        viewModelScope.launch {
            _uiState.value = try {
                MouturesUiState(chargement = false, moutures = repository.chargerMoutures())
            } catch (e: Exception) {
                MouturesUiState(chargement = false, erreur = "Impossible de lire rec.json : ${e.message}")
            }
        }
    }

    /** Un tap sélectionne la mouture ; un second tap sur la même la désélectionne. */
    fun onMoutureClick(mouture: Mouture) {
        _uiState.update { etat ->
            etat.copy(selection = if (etat.selection?.nom == mouture.nom) null else mouture)
        }
    }
}
