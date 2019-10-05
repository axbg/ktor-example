package com.axbg.ctd.services

import com.axbg.ctd.models.Task

interface TaskService {
    fun getAll(): List<Task>
    fun create(task: Task): Task
    fun update(attributes: Map<String, String>): Task
}