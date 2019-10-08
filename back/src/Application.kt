package com.axbg.ctd

import com.axbg.ctd.config.AppException
import com.axbg.ctd.config.Constants
import com.axbg.ctd.controllers.taskController
import com.axbg.ctd.controllers.userController
import com.axbg.ctd.models.*
import com.axbg.ctd.services.TaskService
import com.axbg.ctd.services.TaskServiceImpl
import com.axbg.ctd.services.UserService
import com.axbg.ctd.services.UserServiceImpl
import com.google.gson.Gson
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.client.request.get
import io.ktor.features.CORS
import io.ktor.features.ContentNegotiation
import io.ktor.features.StatusPages
import io.ktor.gson.gson
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.post
import org.jetbrains.exposed.sql.transactions.transaction

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    install(CORS) {
        method(HttpMethod.Options)
        method(HttpMethod.Put)
        method(HttpMethod.Delete)
        method(HttpMethod.Patch)
        header(HttpHeaders.Authorization)
        allowCredentials = true
        anyHost()
    }

    install(ContentNegotiation) {
        gson {
            setPrettyPrinting()
        }
    }

    install(Routing) {
        get("/") {
            call.respond(HttpStatusCode.OK, mapOf("message" to "hourglass back-end"))
        }

        post("/login") {
            val googleToken: GoogleToken = call.receive()
            val googleResponse = HttpClient(Apache).get<String>(Constants.googleOauthCheck + googleToken.token)
            val decodedResponse = Gson().fromJson<GoogleData>(googleResponse, GoogleData::class.java)

            var user: User? = null

            transaction {
                user = User.find { Users.mail eq decodedResponse.email }.firstOrNull()
            }

            when {
                user != null -> {
                    val key = Keys.hmacShaKeyFor(Constants.privateKey.toByteArray())
                    val token = Jwts.builder().setSubject(user!!.id.toString()).signWith(key).compact()
                    call.respond(mapOf("token" to token))
                }
                else -> {
                    throw AppException("User not found", 404)
                }
            }
        }

        userController(ServiceInjector.userService)
        taskController(ServiceInjector.taskService)

        install(StatusPages) {
            exception<AppException> { cause ->
                call.respond(HttpStatusCode.fromValue(cause.status), mapOf("message" to cause.message))
            }
        }
    }

    DatabaseModel.init()
}

class ServiceInjector() {
    companion object {
        val userService: UserService = UserServiceImpl()
        val taskService: TaskService = TaskServiceImpl()
    }
}