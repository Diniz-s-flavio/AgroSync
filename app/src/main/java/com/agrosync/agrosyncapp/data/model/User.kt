package com.agrosync.agrosyncapp.data.model


class User(id: String, firstName: String, lastName: String, email: String) {
    lateinit var id: String
    lateinit var firstName: String
    lateinit var lastName: String
    lateinit var email: String
    lateinit var role: UserRole
    lateinit var owdedFarms: List<Farm>
    lateinit var managerFarms: List<Farm>
}