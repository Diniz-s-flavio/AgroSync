package com.agrosync.agrosyncapp.data.model

import java.io.Serializable
import java.util.Date

class Finance() : Serializable {
    lateinit var id: String
    var resource: Resource = Resource()
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

    override fun toString(): String {
        return "Finance(id='$id', resource=${resource?.toString() ?: "null"}, farm=$farm, user=$user, title='$title', operation=$operation, value=$value, description='$description', date=$date, isFromResource=$isFromResource)"
    }

}