package com.agrosync.agrosyncapp.data.model

class Farm(id: String, name: String, locattion: String, size: Double) {
    lateinit var id: String
    lateinit var name: String
    lateinit var locattion: String
    var size: Double = 0.0
    lateinit var owner: User
    lateinit var managerList: List<User>
}
