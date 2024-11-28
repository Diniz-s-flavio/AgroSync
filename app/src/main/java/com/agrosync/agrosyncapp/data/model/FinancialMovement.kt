package com.agrosync.agrosyncapp.data.model

import java.util.Date

class FinancialMovement(id: String, user: User, title: String, description: String,
                        valeu: Double, operation: Operation,date: Date
) {
    lateinit var id: String
    lateinit var resource: Resource
    lateinit var user: User
    lateinit var title: String
    lateinit var operation: Operation
    var valeu: Double = 0.0
    lateinit var description: String
    var date: Date = Date()
}