package com.exemple.mesfruits.exemple1

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.exemple.mesfruits.R

/** Chapitre 3 : l'Activity hôte qui affiche PremierFragment. */
class Exemple1Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fragments)

        // On n'ajoute le Fragment qu'au PREMIER lancement (pas après une rotation)
        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                replace(R.id.conteneur, PremierFragment())
            }
        }
    }
}
