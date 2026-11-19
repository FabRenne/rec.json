package com.example.moutures2

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

/**
 * Le « dépôt » : il lit rec.json dans les assets et le transforme en List<Mouture>.
 * Il ne sait rien de l'écran.
 */
class MoutureRepository(private val context: Context) {

    /** Lit le fichier sur un fil dédié aux entrées/sorties (pas sur le fil de l'écran). */
    suspend fun chargerMoutures(): List<Mouture> = withContext(Dispatchers.IO) {
        val texte = context.assets.open(FICHIER)
            .bufferedReader(Charsets.UTF_8)
            .use { it.readText() }
        parser(texte)
    }

    companion object {
        const val FICHIER = "rec.json"

        /** Transforme le texte JSON en liste de moutures (testable sans Android). */
        fun parser(json: String): List<Mouture> {
            val racine = JSONObject(json)
            return racine.keys().asSequence().map { nom ->
                val obj = racine.getJSONObject(nom)
                Mouture(
                    nom = nom,
                    sm = obj.optJSONObject("sm").toDosage(),
                    am = obj.optJSONObject("am").toDosage(),
                    info = obj.optJSONObject("info").toInfo(),
                )
            }.toList()
        }

        /** Lit un bloc { "vb": …, "vd": … }. Bloc absent => vitesses à 0. */
        private fun JSONObject?.toDosage(): Dosage = Dosage(
            vb = this?.optInt("vb", 0) ?: 0,
            vd = this?.optInt("vd", 0) ?: 0,
        )

        /** Lit le bloc "info". Bloc absent => tout vide. */
        private fun JSONObject?.toInfo(): InfoMouture {
            if (this == null) {
                return InfoMouture(null, null, null, listOf(false, false, false), false)
            }
            val tableau = optJSONArray("remoulage")
            val remoulage = if (tableau == null) {
                emptyList()
            } else {
                List(tableau.length()) { i -> tableau.optBoolean(i, false) }
            }
            return InfoMouture(
                gruau = texteOuNull("gruau"),
                ameliorant = texteOuNull("ameliorant"),
                mignon = texteOuNull("mignon"),
                remoulage = remoulage,
                devSurete = optBoolean("dev_surete", false),
            )
        }

        /**
         * Attention : optString() renvoie le texte "null" pour une valeur null !
         * isNull() est vrai si la clé manque OU si elle vaut null.
         */
        private fun JSONObject.texteOuNull(cle: String): String? =
            if (isNull(cle)) null else optString(cle)
    }
}
