package com.agrosync.agrosyncapp.data.model

enum class MeasureUnit(val displayName: String) {
    KG("Quilogramas"),
    LITER("Litro"),
    PIECE("Peça"),
    METER("Metro");

    override fun toString(): String {
        return displayName
    }
}
