package com.axbg.ctd.controllers

import com.axbg.ctd.services.TaskService
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.route

fun Routing.taskController(taskService: TaskService) {
    route("/task") {
        get("/") {
            call.respond(HttpStatusCode.OK, "good")
        }
    }
}