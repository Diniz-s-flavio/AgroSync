package com.agrosync.agrosyncapp.data.model

enum class MeasureUnit(val displayName: String) {
    WEIGHT("Peso"),
    PIECE("Peça"),
    LITER("Litro");

    override fun toString(): String {
        return displayName
    }
}
