package com.agrosync.agrosyncapp.data.model

class Resource(id: String, name: String, description: String, farm: Farm, measureUnit: MeasureUnit) {
    lateinit var id: String
    lateinit var name: String
    lateinit var dascription: String
    lateinit var farm: Farm
    var totalValue: Double = 0.0
    var totalAmount: Double = 0.0
    lateinit var measureUnit: MeasureUnit
}