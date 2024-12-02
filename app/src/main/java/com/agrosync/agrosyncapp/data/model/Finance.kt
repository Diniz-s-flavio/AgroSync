package com.agrosync.agrosyncapp.data.model

import java.util.Date

class Finance(id: String, user: User, farm: Farm, title: String, description: String,
              valeu: Double, operation: Operation, date: Date, isFromResource: Boolean
) {
    lateinit var id: String
    lateinit var resource: Resource
    lateinit var farm: Farm
    lateinit var user: User
    lateinit var title: String
    lateinit var operation: Operation
    var valeu: Double = 0.0
    lateinit var description: String
    var date: Date = Date()
    var isFromResource: Boolean = false
}