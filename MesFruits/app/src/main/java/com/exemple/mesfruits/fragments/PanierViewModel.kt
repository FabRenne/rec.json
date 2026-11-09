package com.exemple.mesfruits.fragments

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.exemple.mesfruits.data.Fruit

/**
 * Chapitre 6 : un ViewModel partagé par tous les Fragments de l'Activity
 * (grâce à "by activityViewModels()").
 */
class PanierViewModel : ViewModel() {
    // Liste observable : Compose redessine l'écran quand elle change
    val panier = mutableStateListOf<Fruit>()

    fun ajouter(fruit: Fruit) {
        if (fruit !in panier) panier.add(fruit)
    }

    fun vider() = panier.clear()
}
