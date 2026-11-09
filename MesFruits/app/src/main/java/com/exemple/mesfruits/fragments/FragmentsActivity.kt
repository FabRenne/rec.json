package com.exemple.mesfruits.fragments

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.exemple.mesfruits.R

/** Chapitres 4 à 6 : l'Activity qui héberge ListeFragment, DetailFragment et PanierFragment. */
class FragmentsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fragments)

        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                replace(R.id.conteneur, ListeFragment())
            }
        }
    }
}
