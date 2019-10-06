package com.axbg.ctd.controllers

import com.axbg.ctd.services.TaskService
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.*

fun Routing.taskController(taskService: TaskService) {
    route("/task") {
        get("/") {
            call.respond(HttpStatusCode.OK, taskService.getAll(1))
        }

        post("/") {
            call.respond(HttpStatusCode.Created, taskService.create(call.receive(), 1))
        }

        patch("/") {
            taskService.update(call.receive(), 1)
            call.respond(HttpStatusCode.OK)
        }
    }
}