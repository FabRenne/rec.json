package com.example.crudapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.crudapp.ui.AccueilScreen
import com.example.crudapp.ui.CrudViewModel
import com.example.crudapp.ui.FormulaireScreen
import com.example.crudapp.ui.ListeScreen
import com.example.crudapp.ui.ModeListe

object Routes {
    const val ACCUEIL = "accueil"
    const val AJOUTER = "ajouter"
    const val CHOISIR_MODIF = "choisir_modif"
    const val MODIFIER = "modifier/{id}"
    const val SUPPRIMER = "supprimer"
    fun modifier(id: Long) = "modifier/$id"
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                val nav = rememberNavController()
                // Un seul ViewModel partagé par tous les écrans
                val vm: CrudViewModel = viewModel()

                NavHost(navController = nav, startDestination = Routes.ACCUEIL) {

                    composable(Routes.ACCUEIL) {
                        AccueilScreen(
                            vm = vm,
                            onAjouter = { nav.navigate(Routes.AJOUTER) },
                            onModifier = { nav.navigate(Routes.CHOISIR_MODIF) },
                            onSupprimer = { nav.navigate(Routes.SUPPRIMER) }
                        )
                    }

                    composable(Routes.AJOUTER) {
                        FormulaireScreen(
                            vm = vm,
                            id = null,
                            onRetour = { nav.popBackStack() }
                        )
                    }

                    composable(Routes.CHOISIR_MODIF) {
                        ListeScreen(
                            vm = vm,
                            mode = ModeListe.MODIFIER,
                            onChoisir = { id -> nav.navigate(Routes.modifier(id)) },
                            onRetour = { nav.popBackStack() }
                        )
                    }

                    composable(
                        Routes.MODIFIER,
                        arguments = listOf(navArgument("id") { type = NavType.LongType })
                    ) { entry ->
                        FormulaireScreen(
                            vm = vm,
                            id = entry.arguments?.getLong("id"),
                            onRetour = { nav.popBackStack() }
                        )
                    }

                    composable(Routes.SUPPRIMER) {
                        ListeScreen(
                            vm = vm,
                            mode = ModeListe.SUPPRIMER,
                            onChoisir = {},
                            onRetour = { nav.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}
