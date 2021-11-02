package com.axbg.ctd.services

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import io.ktor.http.*
import java.time.Instant
import java.util.*

class PublicServiceImpl(private val secret: String, private val cookieName: String) : PublicService {
    override fun generateRefreshCookie(userId: Long): Cookie =
        Cookie(name = this.cookieName, value = generateRefreshToken(userId), httpOnly = true, secure = true)

    private fun generateRefreshToken(userId: Long): String {
        val key = Keys.hmacShaKeyFor(this.secret.toByteArray())
        return Jwts.builder()
            .setSubject(userId.toString())
            .setIssuedAt(Date.from(Instant.now()))
            .setExpiration(Date.from(Instant.now().plusSeconds(172800)))
            .signWith(key)
            .compact()
    }
}