package com.exemple.mesrecettes.exemple3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute

class Exemple3Activity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MaterialTheme { AppExemple3() } }
    }
}

@Composable
fun AppExemple3() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Connexion) {

        composable<Connexion> {
            EcranConnexion(
                onConnecte = { prenom ->
                    navController.navigate(AccueilConnecte(prenom)) {
                        // Retire Connexion de la pile (inclusive = true : Connexion compris)
                        popUpTo<Connexion> { inclusive = true }
                        // N'empile pas deux fois le même écran en cas de double clic
                        launchSingleTop = true
                    }
                }
            )
        }

        composable<AccueilConnecte> { backStackEntry ->
            val route: AccueilConnecte = backStackEntry.toRoute()
            EcranAccueilConnecte(
                prenom = route.prenom,
                onDeconnexion = {                                   // exercice 3
                    navController.navigate(Connexion) {
                        // Vide TOUTE la pile, puis affiche Connexion
                        popUpTo(navController.graph.id) { inclusive = true }
                    }
                }
            )
        }
    }
}
