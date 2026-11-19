package com.example.moutures2

/**
 * Le modèle de données : il suit exactement l'imbrication de rec.json.
 *
 *   "Tradition": { "sm": {…}, "am": {…}, "info": {…} }
 *        │            │          │           │
 *     Mouture.nom  Mouture.sm Mouture.am  Mouture.info
 */

/** Un réglage de vitesses (bloc "sm" ou "am" du JSON). */
data class Dosage(
    val vb: Int,   // vitesse blé
    val vd: Int,   // vitesse doseur
)

/** Le bloc "info" du JSON. Le « ? » signifie : peut être null (absent). */
data class InfoMouture(
    val gruau: String?,
    val ameliorant: String?,
    val mignon: String?,
    val remoulage: List<Boolean>,
    val devSurete: Boolean,
)

/** Une mouture complète. Le nom est la clé du JSON ("t55", "Tradition"…). */
data class Mouture(
    val nom: String,
    val sm: Dosage,        // Sans Meule
    val am: Dosage,        // Avec Meule
    val info: InfoMouture,
)
