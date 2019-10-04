package com.axbg.ctd.models

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.IntIdTable

enum class StateType(val status: String) {
    ACTIVE("active"),
    FINISHED("finished"),
    NEXT("next"),
    CANCELED("canceled")
}

object Tasks : IntIdTable() {
    var icon = varchar("icon", 50)
    var text = varchar("text", 255)
    var state = enumerationByName("state", 10, StateType::class.java)
    var user = entityId("user_id", Users)
}

class Task(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Task>(Tasks)

    var icon by Tasks.icon
    var text by Tasks.text
    var state by Tasks.state
    var user by Tasks.user
}