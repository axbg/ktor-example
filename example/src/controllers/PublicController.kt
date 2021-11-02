package com.axbg.ctd.controllers

import com.axbg.ctd.config.AppException
import com.axbg.ctd.config.Constants
import com.axbg.ctd.controllers.dto.GoogleData
import com.axbg.ctd.controllers.dto.GoogleToken
import com.axbg.ctd.models.User
import com.axbg.ctd.models.Users
import com.axbg.ctd.services.PublicService
import com.google.gson.Gson
import io.ktor.application.*
import io.ktor.client.*
import io.ktor.client.engine.apache.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.jetbrains.exposed.sql.transactions.transaction

fun Routing.publicController(publicService: PublicService) {
    route("/") {
        get("/") {
            call.respond(HttpStatusCode.OK, mapOf("message" to "ktor back-end example"))
        }

        post("/login") {
            val googleToken: GoogleToken = call.receive()

            try {
                val googleResponse = HttpClient(Apache).get<String>(Constants.googleOauthCheck + googleToken.token)
                val decodedResponse = Gson().fromJson(googleResponse, GoogleData::class.java)

                var user: User? = null

                transaction {
                    user = User.find { Users.mail eq decodedResponse.email }.firstOrNull()
                }

                when {
                    user != null -> {
                        call.response.cookies.append(publicService.generateRefreshCookie(user!!.id.value))
                        call.respond(HttpStatusCode.OK)
                    }
                    else -> {
                        throw AppException("User not found", 404)
                    }
                }
            } catch (ex: ClientRequestException) {
                throw AppException("Invalid token", 400)
            }
        }
    }
}
