package com.agrosync.agrosyncapp.data.model

class User() {
    var id: String = ""
    var firstName: String = ""
    var lastName: String = ""
    var email: String = ""
    var role: UserRole = UserRole.OWNER
    lateinit var password: String
    var ownedFarms: String? = null
    var managerFarms: List<String> = emptyList()

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