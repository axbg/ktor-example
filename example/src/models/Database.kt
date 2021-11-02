package com.axbg.ctd.models

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils.create
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseModel {
    fun init(dbDriver: String, dbUrl: String, dbUser: String, dbPassword: String) {
        Database.connect(
            dbUrl, driver = dbDriver, user = dbUser, password = dbPassword
        )
        transaction {
            create(Users)
            create(Tasks)
        }
    }
}
