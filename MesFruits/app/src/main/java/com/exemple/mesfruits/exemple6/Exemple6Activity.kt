package com.exemple.mesfruits.exemple6

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.exemple.mesfruits.data.fruitParNom
import com.exemple.mesfruits.data.fruits
import com.exemple.mesfruits.ui.EcranDetail
import com.exemple.mesfruits.ui.EcranListe

/**
 * Chapitre 8 : la même application, SANS aucun Fragment.
 * Les écrans EcranListe et EcranDetail sont exactement ceux des chapitres 4 et 5.
 */
class Exemple6Activity : ComponentActivity() {     // plus besoin d'AppCompatActivity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MaterialTheme { MonApplication() } }
    }
}

@Composable
fun MonApplication() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Accueil) {

        composable<Accueil> {
            EcranListe(
                titre = "Accueil (NavHost)",
                onFruitClique = { fruit -> navController.navigate(Detail(nom = fruit.nom)) }
            )
        }

        composable<Detail> { backStackEntry ->
            val route: Detail = backStackEntry.toRoute()
            EcranDetail(
                fruit = fruitParNom(route.nom),
                onRetour = { navController.popBackStack() },
                onHasard = { navController.navigate(Detail(fruits.random().nom)) }  // exercice 3
            )
        }
    }
}
