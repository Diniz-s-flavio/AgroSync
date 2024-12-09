package com.agrosync.agrosyncapp.data.model

import java.util.Date

class Finance() {
    lateinit var id: String
    lateinit var resource: Resource
    lateinit var farm: Farm
    lateinit var user: User
    lateinit var title: String
    lateinit var operation: Operation
    var value: Double = 0.0
    lateinit var description: String
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