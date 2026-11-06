package com.fabrenne.moutures.data

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject

/** Lit le fichier JSON des moutures placé dans les assets. */
class MoutureRepository(
    private val context: Context,
    private val fileName: String = "rec.json",
) {

    suspend fun chargerMoutures(): List<Mouture> = withContext(Dispatchers.IO) {
        val texte = context.assets.open(fileName).bufferedReader(Charsets.UTF_8).use { it.readText() }
        parser(texte)
    }

    companion object {
        /** L'ordre des clés du fichier est conservé (JSONObject.keys() suit l'ordre de lecture). */
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

        private fun JSONObject?.toDosage() = Dosage(
            vb = this?.optInt("vb", 0) ?: 0,
            vd = this?.optInt("vd", 0) ?: 0,
        )

        private fun JSONObject?.toInfo() = InfoMouture(
            gruau = this?.optStringOrNull("gruau"),
            ameliorant = this?.optStringOrNull("ameliorant"),
            mignon = this?.optStringOrNull("mignon"),
            remoulage = this?.optJSONArray("remoulage").toBooleanList(),
            devSurete = this?.optBoolean("dev_surete", false) ?: false,
        )

        private fun JSONObject.optStringOrNull(cle: String): String? =
            if (isNull(cle)) null else optString(cle).takeIf { it.isNotBlank() }

        private fun JSONArray?.toBooleanList(): List<Boolean> =
            if (this == null) emptyList() else List(length()) { optBoolean(it, false) }
    }
}
