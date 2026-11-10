package com.exemple.stockagecompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.exemple.stockagecompose.ui.AccueilScreen
import com.exemple.stockagecompose.ui.DataStoreScreen
import com.exemple.stockagecompose.ui.FichierScreen
import com.exemple.stockagecompose.ui.PreferencesScreen
import com.exemple.stockagecompose.ui.RoomScreen
import com.exemple.stockagecompose.ui.theme.StockageTheme

/**
 * Point d'entrée de l'application.
 * On affiche un NavHost qui permet de passer d'un écran à l'autre.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StockageTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "accueil") {
        composable("accueil") {
            AccueilScreen(onNavigate = { route -> navController.navigate(route) })
        }
        composable("preferences") { PreferencesScreen(onBack = { navController.popBackStack() }) }
        composable("datastore") { DataStoreScreen(onBack = { navController.popBackStack() }) }
        composable("fichier") { FichierScreen(onBack = { navController.popBackStack() }) }
        composable("room") { RoomScreen(onBack = { navController.popBackStack() }) }
    }
}
