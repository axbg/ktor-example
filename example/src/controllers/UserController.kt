package com.axbg.ctd.controllers

import com.axbg.ctd.UserIdKey
import com.axbg.ctd.models.UserTO
import com.axbg.ctd.services.UserService
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Routing.userController(userService: UserService) {
    route("/user") {
        patch("/") {
            val user: UserTO = call.receive()
            user.id = call.attributes[UserIdKey]
            call.respond(HttpStatusCode.OK, userService.update(user))
        }

        delete("/") {
            userService.delete(call.attributes[UserIdKey])
            call.respond(HttpStatusCode.OK, mapOf("message" to "User deleted"))
        }
    }
}
