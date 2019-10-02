package com.axbg.ctd.controllers

import com.axbg.ctd.*
import com.axbg.ctd.services.*
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*

fun Routing.userController(userService: UserService) {
    route("/user") {
        get("/") {
            call.respond(HttpStatusCode.OK, userService.getUser())
        }
        get("/exception") {
            throw AuthenticationException()
        }
    }
}