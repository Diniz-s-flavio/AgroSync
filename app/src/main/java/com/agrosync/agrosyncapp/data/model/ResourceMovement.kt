package com.agrosync.agrosyncapp.data.model

import java.io.Serializable
import java.util.Date

class ResourceMovement() : Serializable {
    var id: String = ""
    lateinit var resourceId: String
    lateinit var userId: String
    var movementAmount: Double = 0.0
    var oldResourceAmount: Double = 0.0
    var newResourceAmount: Double = 0.0
    var value: Double = 0.0
    lateinit var operation: ResourceOperation
    var movementDate: Date = Date()

    constructor(id: String, resource: String, user: String,
                amount: Double, oldResourceAmount: Double,
                valeu: Double, operation: ResourceOperation,date: Date):this(){
        this.id = id
        this.resourceId = resource
        this.userId = user
        this.movementAmount = amount
        this.oldResourceAmount = oldResourceAmount
        this.value = valeu
        this.operation = operation
        this.movementDate = date
    }
}