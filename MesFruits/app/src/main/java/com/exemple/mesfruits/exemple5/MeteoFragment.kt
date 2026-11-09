package com.exemple.mesfruits.exemple5

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.exemple.mesfruits.R

/**
 * Chapitre 7 : un "ancien" Fragment, écrit avec un layout XML,
 * comme on en trouve dans les applications existantes.
 */
class MeteoFragment : Fragment(R.layout.fragment_meteo) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val ville = arguments?.getString("ville") ?: "Paris"

        view.findViewById<TextView>(R.id.texte_meteo).text =
            "☀ 24 °C — $ville\n(ancien Fragment avec layout XML)"

        view.findViewById<Button>(R.id.bouton_actualiser).setOnClickListener {
            Toast.makeText(requireContext(), "Météo actualisée !", Toast.LENGTH_SHORT).show()
        }
    }
}
