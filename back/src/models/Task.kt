package com.axbg.ctd.models

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.LongIdTable

enum class StateType(val status: String) {
    ACTIVE("active"),
    FINISHED("finished"),
    NEXT("next"),
    CANCELED("canceled")
}

object Tasks : LongIdTable() {
    var icon = varchar("icon", 50)
    var text = varchar("text", 255)
    var state = enumerationByName("state", 10, StateType::class.java)
    var user = reference("user_id", Users)
}

class Task(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<Task>(Tasks)

    var icon by Tasks.icon
    var text by Tasks.text
    var state by Tasks.state
    var user by Tasks.user

    fun transform(): TaskTO {
        return TaskTO(id.value, icon, text, state)
    }
}

data class TaskTO(var id: Long?, var icon: String, var text: String, var state: StateType)