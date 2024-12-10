package com.agrosync.agrosyncapp.data.model

enum class MeasureUnit(val displayName: String, val acronym: String) {
    KG("Quilogramas", "kg"),
    LITER("Litro", "L"),
    PIECE("Peça","un."),
    METER("Metro", "m");

    override fun toString(): String {
        return displayName
    }
}
