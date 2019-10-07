package com.axbg.ctd

import com.axbg.ctd.config.AppException
import com.axbg.ctd.config.Constants
import com.axbg.ctd.controllers.taskController
import com.axbg.ctd.controllers.userController
import com.axbg.ctd.models.DatabaseModel
import com.axbg.ctd.services.TaskService
import com.axbg.ctd.services.TaskServiceImpl
import com.axbg.ctd.services.UserService
import com.axbg.ctd.services.UserServiceImpl
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.auth.*
import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.features.CORS
import io.ktor.features.ContentNegotiation
import io.ktor.features.StatusPages
import io.ktor.gson.gson
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.route
import io.ktor.routing.routing

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

    install(Authentication) {
        oauth("google-oauth") {
            client = HttpClient(Apache)
            providerLookup = { Constants.googleOauthProvider }
            urlProvider = { "http://localhost:8080/login"}
        }
    }

    routing {

    }

    install(Routing) {
        get("/") {
            call.respond(HttpStatusCode.OK, mapOf("message" to "hourglass back-end"))
        }

        authenticate("google-oauth") {
            route("/login") {
                handle {
                    val principal = call.authentication.principal<OAuthAccessTokenResponse.OAuth2>()
                        ?: error("No principal")

                    val json = HttpClient(Apache).get<String>("https://www.googleapis.com/userinfo/v2/me") {
                        header("Authorization", "Bearer $(principal.accessToken)")
                    }


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