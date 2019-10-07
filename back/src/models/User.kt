package com.axbg.ctd.models

import org.jetbrains.exposed.dao.*

object Users : LongIdTable() {
    var mail = varchar("mail", 60)
    var refreshHour = varchar("refresh_hour", 5)
    var notificationHour = varchar("notification_hour", 5)
    var lastUpdated = varchar("last_updated", 11)
}

class User(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<User>(Users)

    var mail by Users.mail
    var refreshFailedException by Users.refreshHour
    var notificationHour by Users.notificationHour
    var lastUpdated by Users.lastUpdated
}

data class UserTO(var id: Long, var mail: String, var refreshHour: String, var notificationHour: String, var lastUpdated: String)