package com.axbg.ctd.services

import com.axbg.ctd.models.TaskTO

interface TaskService {
    fun getAll(userId: Long): List<TaskTO>
    fun create(task: TaskTO, userId: Long): TaskTO
    fun update(task: TaskTO, userId: Long)
}