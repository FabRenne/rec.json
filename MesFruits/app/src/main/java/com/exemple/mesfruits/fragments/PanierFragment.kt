package com.exemple.mesfruits.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.material3.MaterialTheme
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.compose.content
import com.exemple.mesfruits.ui.EcranPanier

/** Chapitre 6 : affiche le panier partagé via le ViewModel de l'Activity. */
class PanierFragment : Fragment() {

    // "activityViewModels" = partagé avec tous les Fragments de l'Activity
    private val vm: PanierViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = content {
        MaterialTheme {
            EcranPanier(
                fruits = vm.panier,
                onVider = vm::vider,
                onRetour = { parentFragmentManager.popBackStack() }
            )
        }
    }
}
