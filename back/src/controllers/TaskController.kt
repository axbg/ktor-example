package com.axbg.ctd.controllers

import com.axbg.ctd.models.Task
import com.axbg.ctd.services.TaskService
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.*

fun Routing.taskController(taskService: TaskService) {
    route("/task") {
        get("/") {
            val tasks: List<Task> = taskService.getAll()
            call.respond(HttpStatusCode.OK, tasks)
        }

        post("/") {
            val task: Task = call.receive()
            println(task)
            call.respond(HttpStatusCode.OK, taskService.create(task))
        }

        patch("/") {
            val updatedAttributes: Map<String, String> = call.receive()
            call.respond(HttpStatusCode.OK, taskService.update(updatedAttributes))
        }
    }
}