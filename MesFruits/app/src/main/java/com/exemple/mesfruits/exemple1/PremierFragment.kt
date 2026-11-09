package com.exemple.mesfruits.exemple1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import com.exemple.mesfruits.ui.EcranPremierFragment

/**
 * Chapitre 3 : version "longue" avec ComposeView.
 * (Les autres Fragments du projet utilisent le raccourci content { }.)
 */
class PremierFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            // Libère Compose quand la vue du Fragment est détruite (chapitre 9)
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
            )
            setContent {
                MaterialTheme {
                    EcranPremierFragment()
                }
            }
        }
    }
}
