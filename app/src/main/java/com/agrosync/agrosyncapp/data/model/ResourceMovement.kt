package com.agrosync.agrosyncapp.data.model

import java.util.Date

class ResourceMovement(id: String, resource: Resource, user: User,
                       amount: Double,valeu: Double, operation: Operation,date: Date) {
    lateinit var id: String
    lateinit var resource: Resource
    lateinit var user: User
    var amount: Double = 0.0
    var valeu: Double = 0.0
    lateinit var operation: Operation
    var date: Date = Date()
}