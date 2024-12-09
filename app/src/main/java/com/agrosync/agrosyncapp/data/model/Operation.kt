package com.agrosync.agrosyncapp.data.model

enum class Operation(val displayName: String) {
    ENTRY("Entrada"),
    WITHDRAWAL("Saída");

    override fun toString(): String {
        return displayName
    }
}
