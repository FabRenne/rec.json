@file:OptIn(ExperimentalMaterial3Api::class)

package com.exemple.mesrecettes.exemple4

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.exemple.mesrecettes.ui.BarreDuHaut

class Exemple4Activity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MaterialTheme { AppExemple4() } }
    }
}

@Composable
fun AppExemple4() {
    val navController = rememberNavController()

    // L'écran actuellement affiché (se met à jour à chaque navigation)
    val entreeActuelle by navController.currentBackStackEntryAsState()
    val destinationActuelle = entreeActuelle?.destination

    // L'onglet sélectionné = celui dont la route correspond à la destination actuelle
    val ongletActuel = onglets.firstOrNull { onglet ->
        destinationActuelle?.hierarchy?.any { it.hasRoute(onglet.route::class) } == true
    }

    Scaffold(
        topBar = { BarreDuHaut(ongletActuel?.titre ?: "") },
        bottomBar = {
            NavigationBar {
                onglets.forEach { onglet ->
                    NavigationBarItem(
                        selected = onglet == ongletActuel,
                        onClick = {
                            navController.navigate(onglet.route) {
                                // Revient au 1er onglet en gardant l'état des autres
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true   // pas de doublon si on re-touche l'onglet
                                restoreState = true      // retrouve l'état (ex. position de la liste)
                            }
                        },
                        icon = { Icon(onglet.icone, contentDescription = onglet.titre) },
                        label = { Text(onglet.titre) }
                    )
                }
            }
        }
    ) { padding ->
        // Le NavHost occupe l'espace entre les deux barres
        NavHost(
            navController = navController,
            startDestination = Recettes,
            modifier = Modifier.padding(padding)
        ) {
            composable<Recettes> { EcranRecettes() }
            composable<Favoris> { EcranFavoris() }
            composable<Profil> { EcranProfil() }
        }
    }
}
