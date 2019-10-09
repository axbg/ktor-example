package com.axbg.ctd

import com.axbg.ctd.config.AppException
import com.axbg.ctd.config.Constants
import com.axbg.ctd.controllers.publicController
import com.axbg.ctd.controllers.taskController
import com.axbg.ctd.controllers.userController
import com.axbg.ctd.models.DatabaseModel
import com.axbg.ctd.models.User
import com.axbg.ctd.services.TaskService
import com.axbg.ctd.services.TaskServiceImpl
import com.axbg.ctd.services.UserService
import com.axbg.ctd.services.UserServiceImpl
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import io.ktor.application.Application
import io.ktor.application.ApplicationCallPipeline
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.CORS
import io.ktor.features.ContentNegotiation
import io.ktor.features.StatusPages
import io.ktor.gson.gson
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.request.path
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.util.AttributeKey
import org.jetbrains.exposed.sql.transactions.transaction

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

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
        intercept(ApplicationCallPipeline.Setup) {
            if (!Constants.publicRoutes.contains(call.request.path())) {
                val authorization = call.request.headers["Authorization"] ?: throw AppException("Token not present", 400)
                val token = authorization.split(" ")[1]
                val key = Keys.hmacShaKeyFor(Constants.privateKey.toByteArray())
                try {
                    val claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token).body

                    transaction {
                        User.findById(claims.subject.toLong()) ?: throw AppException("User not found", 404)
                    }

                    call.attributes.put(UserIdKey, claims.subject.toLong())
                } catch (ex: JwtException) {
                    throw AppException("JWT is not valid", 400)
                }
            }
        }

        publicController()
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

val UserIdKey = AttributeKey<Long>("userId")

class ServiceInjector() {
    companion object {
        val userService: UserService = UserServiceImpl()
        val taskService: TaskService = TaskServiceImpl()
    }
}