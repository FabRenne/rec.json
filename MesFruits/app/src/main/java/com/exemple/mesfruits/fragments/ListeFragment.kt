package com.exemple.mesfruits.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.material3.MaterialTheme
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.fragment.compose.content
import com.exemple.mesfruits.R
import com.exemple.mesfruits.ui.EcranListe

/** Chapitre 4 : l'écran 1, la liste des fruits. */
class ListeFragment : Fragment() {

    private val vm: PanierViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = content {
        MaterialTheme {
            EcranListe(
                onFruitClique = { fruit -> ouvrir(DetailFragment.nouveau(fruit.nom)) },
                nbDansPanier = vm.panier.size,
                onPanier = { ouvrir(PanierFragment()) }
            )
        }
    }

    private fun ouvrir(fragment: Fragment) {
        parentFragmentManager.commit {
            setReorderingAllowed(true)             // bonne pratique
            replace(R.id.conteneur, fragment)      // on remplace l'écran
            addToBackStack(null)                   // pour que « Retour » marche
        }
    }
}
