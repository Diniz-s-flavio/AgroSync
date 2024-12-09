package com.agrosync.agrosyncapp.data.model

import android.os.Bundle

class Farm() {
    var id: String? = ""
    var owner: User? = null
    var name: String? = ""
    var location: String? = ""
    var size: Double? = null
    var managerList: List<User>? = null

    public constructor(id: String, owner: User) : this(){
        this.id = id
        this.owner = owner
    }

    fun toBundle(): Bundle {
        val bundle = Bundle()
        bundle.putString("id", id)
        bundle.putString("ownerId", owner?.id)

        return bundle
    }

    companion object {
        fun fromBundle(bundle: Bundle): Farm? {
            val farm = Farm()
            farm.id = bundle.getString("id")
            farm.name = bundle.getString("name")

            bundle.getBundle("owner")?.let {
                farm.owner = User.fromBundle(it) // Supondo que User também tenha um `fromBundle`
            }

            return farm
        }
    }
}
