package com.axbg.ctd.services

import com.axbg.ctd.config.AppException
import com.axbg.ctd.models.Task
import com.axbg.ctd.models.TaskTO
import com.axbg.ctd.models.Tasks
import com.axbg.ctd.models.Users
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.sql.transactions.transaction

class TaskServiceImpl : TaskService {
    override fun getAll(userId: Long): List<TaskTO> {
        return transaction {
            Task.find { Tasks.user.eq(userId) }
                .map { task ->
                    task.transform()
                }.toList()
        }
    }

    override fun create(task: TaskTO, userId: Long): TaskTO {
        try {
            transaction {
                task.id = Task.new {
                    icon = task.icon
                    text = task.text
                    state = task.state
                    user = EntityID(userId, Users)
                }.id.value
            }
        } catch (e: Exception) {
            throw AppException("An error occured when inserting", 500)
        }
        return task
    }

    override fun update(task: TaskTO, userId: Long) {
        try {
            transaction {
                val taskEntity = Task.findById(task.id!!)
                if (taskEntity!!.user.value == userId) {
                    taskEntity.icon = task.icon
                    taskEntity.text = task.text
                    taskEntity.state = task.state
                } else {
                    throw NullPointerException()
                }
            }
        } catch (e: NullPointerException) {
            throw AppException("Task was not found", 404)
        } catch (e: Exception) {
            throw AppException("An error occured when updating", 500)
        }
    }
}