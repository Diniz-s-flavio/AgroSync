package com.agrosync.agrosyncapp.data.model

class Resource(var id: String,var  name: String,var description: String,
               var category: String, var  farm: Farm,var  measureUnit: MeasureUnit) {
    var imgUrl: String = ""
    var totalValue: Double = 0.0
    var totalAmount: Double = 0.0
}