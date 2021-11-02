package com.axbg.ctd.controllers

import com.axbg.ctd.UserIdKey
import com.axbg.ctd.services.TaskService
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Routing.taskController(taskService: TaskService) {
    route("/task") {
        get("/") {
            call.respond(HttpStatusCode.OK, taskService.getAll(call.attributes[UserIdKey]))
        }

        post("/") {
            call.respond(HttpStatusCode.Created, taskService.create(call.receive(), call.attributes[UserIdKey]))
        }

        patch("/") {
            taskService.update(call.receive(), call.attributes[UserIdKey])
            call.respond(HttpStatusCode.OK)
        }
    }
}
