package com.agrosync.agrosyncapp.data.model

import android.os.Bundle

class Resource() {
    var id: String = ""
    var name: String = ""
    var description: String = ""
    var category: String = ""
    var farm: Farm? = null
    var measureUnit: MeasureUnit = MeasureUnit.KG
    var imgUrl: String = ""
    var totalValue: Double = 0.0
    var totalAmount: Double = 0.0

    constructor(
        id: String,
        name: String,
        description: String,
        category: String,
        farm: Farm,
        measureUnit: MeasureUnit) : this(){
            this.id = id
            this.name = name
            this.description = description
            this.category = category
            this.farm = farm
            this.measureUnit = measureUnit
        }
    fun toBundle(): Bundle {
        val bundle = Bundle()
        bundle.putString("id", id)
        bundle.putString("name", name)
        bundle.putString("description", description)
        bundle.putString("category", category)
        bundle.putString("measureUnit", measureUnit.name)
        bundle.putString("imgUrl", imgUrl)
        bundle.putDouble("totalValue", totalValue)
        bundle.putDouble("totalAmount", totalAmount)

        farm?.let {
            bundle.putBundle("farm", it.toBundle()) // Supondo que Farm também tenha um método toBundle()
        }

        return bundle
    }

    companion object {
        fun fromBundle(bundle: Bundle): Resource{
            val resource = Resource()
            resource.id = bundle.getString("id") ?: ""
            resource.name = bundle.getString("name") ?: ""
            resource.description = bundle.getString("description") ?: ""
            resource.category = bundle.getString("category") ?: ""
            resource.measureUnit = bundle.getString("measureUnit")?.let { MeasureUnit.valueOf(it) } ?: MeasureUnit.KG
            resource.imgUrl = bundle.getString("imgUrl") ?: ""
            resource.totalValue = bundle.getDouble("totalValue", 0.0)
            resource.totalAmount = bundle.getDouble("totalAmount", 0.0)

            bundle.getBundle("farm")?.let {
                resource.farm = Farm.fromBundle(it) // Supondo que Farm também tenha um método `fromBundle`
            }

            return resource
        }
    }
}