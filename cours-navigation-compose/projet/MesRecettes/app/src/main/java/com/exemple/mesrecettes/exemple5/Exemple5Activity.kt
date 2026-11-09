package com.exemple.mesrecettes.exemple5

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.exemple.mesrecettes.data.recetteParId

/** Clé utilisée pour ranger la note dans le SavedStateHandle. */
const val CLE_NOTE = "note"

class Exemple5Activity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MaterialTheme { AppExemple5() } }
    }
}

@Composable
fun AppExemple5() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Liste) {

        composable<Liste> {
            EcranListe(onRecetteClique = { navController.navigate(Detail(it.id)) })
        }

        composable<Detail> { backStackEntry ->
            val recette = recetteParId(backStackEntry.toRoute<Detail>().id)

            // 2. On ÉCOUTE la note déposée dans NOTRE SavedStateHandle (0 = pas encore notée)
            val note by backStackEntry.savedStateHandle
                .getStateFlow(CLE_NOTE, 0)
                .collectAsState()

            EcranDetailAvecNote(
                recette = recette,
                note = note,
                onNoter = { navController.navigate(Noter(recette.id)) },
                onRetour = { navController.popBackStack() }
            )
        }

        composable<Noter> { backStackEntry ->
            val recette = recetteParId(backStackEntry.toRoute<Noter>().id)
            EcranNoter(
                recette = recette,
                onValider = { noteChoisie ->
                    // 1. On DÉPOSE la note dans le SavedStateHandle de l'écran PRÉCÉDENT…
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set(CLE_NOTE, noteChoisie)
                    // … puis on revient en arrière
                    navController.popBackStack()
                },
                onRetour = { navController.popBackStack() }
            )
        }
    }
}
