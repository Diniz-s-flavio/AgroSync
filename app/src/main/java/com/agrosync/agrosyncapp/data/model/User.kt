package com.agrosync.agrosyncapp.data.model

import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.IgnoreExtraProperties


class User() {
    var id: String = ""
    var firstName: String = ""
    var lastName: String = ""
    var email: String = ""
    var role: UserRole = UserRole.OWNER
    lateinit var password: String
    var ownedFarms: Farm? = null
    lateinit var managerFarms: List<Farm>
    constructor(id: String, firstName: String, lastName: String, email: String, role: UserRole) : this() {
        this.id = id
        this.firstName = firstName
        this.lastName = lastName
        this.email = email
        this.role = role
    }
}