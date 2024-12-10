package com.agrosync.agrosyncapp.data.model

enum class ResourceOperation(val displayName: String) {
    BUY("Compra"),
    SELL("Venda"),
    ENTRY("Entrada"),
    WITHDRAWAL("Saída");

    override fun toString(): String {
        return displayName
    }
}
