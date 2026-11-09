package com.exemple.mesrecettes.exemple1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class Exemple1Activity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MaterialTheme { AppExemple1() } }
    }
}

@Composable
fun AppExemple1() {
    // 1. Le NavController : le "chauffeur" qui change d'écran
    val navController = rememberNavController()

    // 2. Le NavHost : la "carte" de tous les écrans
    NavHost(navController = navController, startDestination = Accueil) {

        composable<Accueil> {
            EcranAccueil(
                onVoirProfil = { navController.navigate(Profil) }
            )
        }

        composable<Profil> {
            EcranProfil(
                onRetour = { navController.popBackStack() },
                onParametres = { navController.navigate(Parametres) }   // exercice 1
            )
        }

        composable<Parametres> {                                        // exercice 1
            EcranParametres(onRetour = { navController.popBackStack() })
        }
    }
}
