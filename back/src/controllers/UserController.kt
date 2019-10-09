package com.axbg.ctd.controllers

import com.axbg.ctd.UserIdKey
import com.axbg.ctd.models.UserTO
import com.axbg.ctd.services.UserService
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.delete
import io.ktor.routing.patch
import io.ktor.routing.route

fun Routing.userController(userService: UserService) {
    route("/user") {
        patch("/") {
            val user: UserTO = call.receive()
            user.id = call.attributes[UserIdKey]
            call.respond(HttpStatusCode.OK, userService.update(user))
        }

        delete("/") {
            userService.delete(call.attributes[UserIdKey])
            call.respond(HttpStatusCode.OK, mapOf("message" to "user deleted"))
        }
    }
}