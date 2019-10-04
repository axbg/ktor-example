package com.axbg.ctd.models

import com.axbg.ctd.config.Constants
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils.create
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseModel {
    fun init() {
        Database.connect(
            Constants.DATABASE_URL, driver = Constants.DATABASE_DRIVER, user = Constants.DATABASE_USERNAME,
            password = Constants.DATABASE_PASSWORD
        )
        transaction {
            create(Users)
            create(Tasks)
        }
    }
}