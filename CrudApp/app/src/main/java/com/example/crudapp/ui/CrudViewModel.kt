package com.example.crudapp.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.crudapp.data.DatabaseHelper
import com.example.crudapp.data.Enregistrement
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CrudViewModel(application: Application) : AndroidViewModel(application) {

    private val db = DatabaseHelper(application)

    private val _nombre = MutableStateFlow(0L)
    val nombre: StateFlow<Long> = _nombre.asStateFlow()

    private val _enregistrements = MutableStateFlow<List<Enregistrement>>(emptyList())
    val enregistrements: StateFlow<List<Enregistrement>> = _enregistrements.asStateFlow()

    init {
        rafraichir()
    }

    /** Relit le nombre d'enregistrements et la liste depuis SQLite. */
    fun rafraichir() {
        viewModelScope.launch {
            val (n, liste) = withContext(Dispatchers.IO) { db.compter() to db.lireTous() }
            _nombre.value = n
            _enregistrements.value = liste
        }
    }

    suspend fun lireParId(id: Long): Enregistrement? =
        withContext(Dispatchers.IO) { db.lireParId(id) }

    fun ajouter(nom: String, date: String, onFini: () -> Unit) = executer(onFini) {
        db.ajouter(Enregistrement(nom = nom, date = date))
    }

    fun modifier(id: Long, nom: String, date: String, onFini: () -> Unit) = executer(onFini) {
        db.modifier(Enregistrement(id = id, nom = nom, date = date))
    }

    fun supprimer(id: Long) = executer {
        db.supprimer(id)
    }

    private fun executer(onFini: () -> Unit = {}, action: () -> Unit) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) { action() }
            rafraichir()
            onFini()
        }
    }

    override fun onCleared() {
        db.close()
        super.onCleared()
    }
}
