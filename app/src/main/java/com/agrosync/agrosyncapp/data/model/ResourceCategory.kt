package com.agrosync.agrosyncapp.data.model

enum class ResourceCategory(val displayName: String) {
    FERTILIZERS("Adubos e Fertilizantes"),
    SEEDS("Sementes e Mudas"),
    PESTICIDES("Defensivos Agrícolas"),
    ANIMAL_FEED("Rações e Suplementos Animais"),
    EQUIPMENT("Equipamentos e Ferramentas"),
    VETERINARY_PRODUCTS("Produtos Veterinários"),
    FUELS("Combustíveis e Lubrificantes"),
    CONSTRUCTION_MATERIALS("Materiais de Construção Rural"),
    OTHERS("Outros");

    override fun toString(): String {
        return displayName
    }
}