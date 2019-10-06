package com.axbg.ctd.models

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.IntIdTable

object Users : IntIdTable() {
    var mail = varchar("mail", 60)
    var refreshHour = varchar("refresh_hour", 5)
    var notificationHour = varchar("notification_hour", 5)
    var lastUpdated = varchar("last_updated", 11)
}

class User(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<User>(Users)

    var mail by Users.mail
    var refreshFailedException by Users.refreshHour
    var notificationHour by Users.notificationHour
    var lastUpdated by Users.lastUpdated
}