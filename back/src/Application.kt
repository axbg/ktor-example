package com.axbg.ctd

import com.axbg.ctd.controllers.taskController
import com.axbg.ctd.controllers.userController
import com.axbg.ctd.models.DatabaseModel
import com.axbg.ctd.services.TaskService
import com.axbg.ctd.services.TaskServiceImpl
import com.axbg.ctd.services.UserService
import com.axbg.ctd.services.UserServiceImpl
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.auth.Authentication
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
    }

    install(Routing) {
        get("/") {
            call.respond(HttpStatusCode.OK, mapOf("message" to "hourglass back-end"))
        }

        userController(ServiceInjector.userService)
        taskController(ServiceInjector.taskService)
        install(StatusPages) {
            exception<AuthenticationException> { cause ->
                call.respond(HttpStatusCode.Unauthorized, mapOf("message" to "not allowed"))
            }
            exception<AuthorizationException> { cause ->
                call.respond(HttpStatusCode.Forbidden)
            }
        }
    }

    DatabaseModel.init()
}

class AuthenticationException : RuntimeException()
class AuthorizationException : RuntimeException()

class ServiceInjector() {
    companion object {
        val userService: UserService = UserServiceImpl()
        val taskService: TaskService = TaskServiceImpl()
    }
}