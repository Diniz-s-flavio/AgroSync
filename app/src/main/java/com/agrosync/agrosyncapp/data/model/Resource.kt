package com.agrosync.agrosyncapp.data.model

import android.os.Bundle
import java.util.Locale
import java.text.NumberFormat

class Resource() {
    var id: String = ""
    var name: String = ""
    var description: String = ""
    var category: ResourceCategory = ResourceCategory.OTHERS
    var farm: Farm? = null
    var measureUnit: MeasureUnit = MeasureUnit.KG
    var imgUrl: String = ""
    var totalValue: Double = 0.0
    var totalAmount: Double = 0.0

    constructor(
        id: String,
        name: String,
        description: String,
        category: ResourceCategory,
        farm: Farm,
        measureUnit: MeasureUnit) : this(){
            this.id = id
            this.name = name
            this.description = description
            this.category = category
            this.farm = farm
            this.measureUnit = measureUnit
        }

    override fun toString(): String {
        return "Resource(id='$id', name='$name', description='$description', category='$category', farm=$farm, measureUnit=$measureUnit, imgUrl='$imgUrl', totalValue=$totalValue, totalAmount=$totalAmount)"
    }

    fun toBundle(): Bundle {
        val bundle = Bundle()
        bundle.putString("id", id)
        bundle.putString("name", name)
        bundle.putString("description", description)
        bundle.putString("category", category.displayName)
        bundle.putString("measureUnit", measureUnit.name)
        bundle.putString("imgUrl", imgUrl)
        bundle.putDouble("totalValue", totalValue)
        bundle.putDouble("totalAmount", totalAmount)

        farm?.let {
            bundle.putBundle("farm", it.toBundle())
        }

        return bundle
    }

    fun formatToCurrency(): String {
        val format = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
        return format.format(totalValue)
    }

    fun calcResourceNewAmount(operation: ResourceOperation, movementAmount: Double) {
        if (operation == ResourceOperation.WITHDRAWAL || operation == ResourceOperation.SELL){
            if (this.totalAmount < movementAmount){
                this.totalAmount = 0.0
            }else this.totalAmount -= movementAmount
        } else {
            this.totalAmount += movementAmount

        }
    }

    fun calcResourceNewTotalValue(operation: ResourceOperation, movementValue: Double) {
        if (operation == ResourceOperation.WITHDRAWAL || operation == ResourceOperation.SELL){
            this.totalValue -= movementValue
        } else {
            this.totalValue += movementValue
        }

        if (totalAmount == 0.0){
            this.totalValue = 0.0
        }
    }
}