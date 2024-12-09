package com.agrosync.agrosyncapp.data.model

import java.util.Date

class Finance() {
    var id: String = ""
    var resource: Resource? = null
    var farm: Farm? = null
    var user: User? = null
    var title: String = ""
    var operation: Operation? = null
    var value: Double = 0.0
    var description: String = ""
    var date: Date = Date()
    var isFromResource: Boolean = false


    public constructor(id: String, user: User, farm: Farm, title: String, description: String,
                       value: Double, operation: Operation, date: Date, isFromResource: Boolean) : this(){
        this.id = id
        this.user = user
        this.farm = farm
        this.title = title
        this.description = description
        this.value = value
        this.operation = operation
        this.date = date
        this.isFromResource = isFromResource
    }

}