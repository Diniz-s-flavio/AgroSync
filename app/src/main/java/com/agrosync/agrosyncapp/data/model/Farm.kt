package com.agrosync.agrosyncapp.data.model

class Farm(var id: String, var owner: User) {
    lateinit var name: String
    lateinit var locattion: String
    var size: Double = 0.0
    lateinit var managerList: List<User>
}
