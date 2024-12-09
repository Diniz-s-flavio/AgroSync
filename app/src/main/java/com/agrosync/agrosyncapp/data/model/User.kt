package com.agrosync.agrosyncapp.data.model

class User() {
    var id: String = ""
    var firstName: String = ""
    var lastName: String = ""
    var email: String = ""
    var role: UserRole = UserRole.OWNER
    lateinit var password: String
    var ownedFarms: String? = null // Alterado para armazenar o ID da fazenda como string
    var managerFarms: List<String> =
        emptyList() // Alterado para armazenar IDs como lista de strings

    constructor(
        id: String,
        firstName: String,
        lastName: String,
        email: String,
        role: UserRole
    ) : this() {
        this.id = id
        this.firstName = firstName
        this.lastName = lastName
        this.email = email
        this.role = role
    }
}