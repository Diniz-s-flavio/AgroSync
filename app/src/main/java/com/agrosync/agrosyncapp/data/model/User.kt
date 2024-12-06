package com.agrosync.agrosyncapp.data.model


data class User(var id: String, var firstName: String, var lastName: String, var email: String, var role: UserRole) {
    lateinit var password: String
    var ownedFarms: Farm? = null
    lateinit var managerFarms: List<Farm>
}