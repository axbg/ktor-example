package com.axbg.ctd.services

import com.axbg.ctd.models.StateType
import com.axbg.ctd.models.Task
import com.axbg.ctd.models.Tasks
import com.axbg.ctd.models.Users
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction

class TaskServiceImpl : TaskService {
    override fun getAll(): List<Task> {
        val tasks = mutableListOf<Task>()

        transaction {
            val task = Task.new {
                icon = "starting"
                text = "asdasd"
                state = StateType.ACTIVE
                user = EntityID(1, Users)
            }
        }

        return transaction {
            Task.all().asSequence().toList()
        }
    }

    override fun create(task: Task): Task {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun update(attributes: Map<String, String>): Task {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}