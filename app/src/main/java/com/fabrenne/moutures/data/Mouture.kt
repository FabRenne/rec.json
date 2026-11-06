package com.fabrenne.moutures.data

/** Valeurs vb / vd d'une période (sm ou am). */
data class Dosage(
    val vb: Int,
    val vd: Int,
)

/** Informations complémentaires d'une mouture. */
data class InfoMouture(
    val gruau: String?,
    val ameliorant: String?,
    val mignon: String?,
    val remoulage: List<Boolean>,
    val devSurete: Boolean,
)

/** Une mouture telle que décrite dans rec.json. */
data class Mouture(
    val nom: String,
    val sm: Dosage,
    val am: Dosage,
    val info: InfoMouture,
)
