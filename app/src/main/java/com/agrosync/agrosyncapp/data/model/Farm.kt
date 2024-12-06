package com.agrosync.agrosyncapp.data.model

class Farm() {
    var id: String? = ""
    var owner: User? = null
    var name: String? = ""
    var locattion: String? = ""
    var size: Double? = null
    var managerList: List<User>? = null

    public constructor(id: String, owner: User) : this(){
        this.id = id
        this.owner = owner
    }
}
