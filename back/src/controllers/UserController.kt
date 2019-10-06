package com.axbg.ctd.controllers

import com.axbg.ctd.services.UserService
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.*

fun Routing.userController(userService: UserService) {
    route("/user") {
        post("/") {
            userService.login();
        }

        patch("/") {
            val body : Map<String, String> = call.receive()
            //also send user id after authentication
            userService.update(body["refreshHour"], body["notificationHour"])
        }

        delete("/") {
            //get user id from authentication
            userService.delete(1)
        }
    }
}