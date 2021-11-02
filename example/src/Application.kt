package com.axbg.ctd

import com.axbg.ctd.config.AppException
import com.axbg.ctd.config.Constants
import com.axbg.ctd.controllers.publicController
import com.axbg.ctd.controllers.taskController
import com.axbg.ctd.controllers.userController
import com.axbg.ctd.models.DatabaseModel
import com.axbg.ctd.models.User
import com.axbg.ctd.services.PublicServiceImpl
import com.axbg.ctd.services.TaskServiceImpl
import com.axbg.ctd.services.UserServiceImpl
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.util.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.Instant
import java.util.*

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
                val token = call.request.cookies[environment.config.property("ktor.jwt.cookie_name").getString()]
                val key = Keys.hmacShaKeyFor(environment.config.property("ktor.jwt.secret").getString().toByteArray())
                try {
                    val claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).body
                    transaction {
                        User.findById(claims.subject.toLong()) ?: throw AppException("User not found", 404)
                    }

                    call.attributes.put(UserIdKey, claims.subject.toLong())

                    if (claims.expiration > Date.from(Instant.now().minusSeconds(172000))) {
                        throw AppException("Authentication expired", 400)
                    }
                } catch (ex: JwtException) {
                    throw AppException("Authentication invalid", 400)
                }
            }
        }

        userController(UserServiceImpl())
        taskController(TaskServiceImpl())
        publicController(
            PublicServiceImpl(
                environment.config.property("ktor.jwt.secret").getString(),
                environment.config.property("ktor.jwt.cookie_name").getString()
            )
        )

        install(StatusPages) {
            exception<AppException> { cause ->
                call.respond(HttpStatusCode.fromValue(cause.status), mapOf("message" to cause.message))
            }
        }
    }

    val dbDriver = environment.config.property("ktor.db.driver").getString()
    val dbUrl = environment.config.property("ktor.db.url").getString()
    val dbUser = environment.config.property("ktor.db.username").getString()
    DatabaseModel.init(dbDriver, dbUrl, dbUser, environment.config.property("ktor.db.password").getString())
}

val UserIdKey = AttributeKey<Long>("userId")
