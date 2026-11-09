package com.exemple.mesrecettes.exemple2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.exemple.mesrecettes.data.recetteParId

class Exemple2Activity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MaterialTheme { AppExemple2() } }
    }
}

@Composable
fun AppExemple2() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = ListeRecettes) {

        composable<ListeRecettes> {
            EcranListe(
                onRecetteClique = { recette ->
                    // On crée la destination AVEC sa valeur
                    navController.navigate(Detail(id = recette.id))
                }
            )
        }

        composable<Detail> { backStackEntry ->
            // On relit l'argument, déjà avec le bon type (Int)
            val detail: Detail = backStackEntry.toRoute()
            val recette = recetteParId(detail.id)
            EcranDetail(
                recette = recette,
                onRetour = { navController.popBackStack() },
                onVoirAuteur = { navController.navigate(Auteur(nom = recette.auteur)) }  // exercice 2
            )
        }

        composable<Auteur> { backStackEntry ->                                         // exercice 2
            val auteur: Auteur = backStackEntry.toRoute()
            EcranAuteur(nom = auteur.nom, onRetour = { navController.popBackStack() })
        }
    }
}
