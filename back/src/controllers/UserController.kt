package com.axbg.ctd.controllers

import com.axbg.ctd.UserIdKey
import com.axbg.ctd.models.UserTO
import com.axbg.ctd.services.UserService
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.*

fun Routing.userController(userService: UserService) {
    route("/user") {
        patch("/") {
            val user: UserTO = call.receive()

        }

        delete("/") {
            userService.delete(call.attributes[UserIdKey])
            call.respond(HttpStatusCode.OK, mapOf("message" to "user deleted"))
        }
    }
}