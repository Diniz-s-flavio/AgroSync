package com.agrosync.agrosyncapp.data.model

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
}