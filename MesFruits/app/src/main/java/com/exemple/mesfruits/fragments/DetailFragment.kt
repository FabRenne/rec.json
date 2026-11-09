package com.exemple.mesfruits.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.material3.MaterialTheme
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.fragment.compose.content
import com.exemple.mesfruits.R
import com.exemple.mesfruits.data.fruitParNom
import com.exemple.mesfruits.data.fruits
import com.exemple.mesfruits.ui.EcranDetail

/** Chapitre 5 : l'écran 2, qui reçoit le nom du fruit dans ses arguments. */
class DetailFragment : Fragment() {

    companion object {
        private const val CLE_NOM = "nom"

        // "Usine" : la seule façon de créer le Fragment avec ses données.
        // (JAMAIS de paramètre dans le constructeur d'un Fragment !)
        fun nouveau(nom: String) = DetailFragment().apply {
            arguments = bundleOf(CLE_NOM to nom)
        }
    }

    // Chapitre 6 : le MÊME ViewModel que ListeFragment et PanierFragment
    private val vm: PanierViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = content {
        // On relit la donnée dans les arguments
        val nom = requireArguments().getString(CLE_NOM)!!
        val fruit = fruitParNom(nom)

        MaterialTheme {
            EcranDetail(
                fruit = fruit,
                onRetour = { parentFragmentManager.popBackStack() },
                onAjouterPanier = { vm.ajouter(fruit) },
                onHasard = { ouvrirAuHasard() }          // exercice 2
            )
        }
    }

    private fun ouvrirAuHasard() {
        val autre = fruits.random()
        parentFragmentManager.commit {
            setReorderingAllowed(true)
            replace(R.id.conteneur, nouveau(autre.nom))
            addToBackStack(null)
        }
    }
}
