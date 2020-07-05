package com.axbg.ctd.services

import com.axbg.ctd.config.Constants
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import io.ktor.http.Cookie
import java.time.Instant
import java.util.*

class PublicServiceImpl : PublicService {
    override fun generateRefreshCookie(userId: Long): Cookie =
        Cookie(name = Constants.TOKEN_COOKIE, value = generateRefreshToken(userId), httpOnly = true, secure = true)

    private fun generateRefreshToken(userId: Long): String {
        val key = Keys.hmacShaKeyFor(Constants.TOKEN_ENC_KEY.toByteArray())
        return Jwts.builder()
            .setSubject(userId.toString())
            .setIssuedAt(Date.from(Instant.now()))
            .setExpiration(Date.from(Instant.now().plusSeconds(172800)))
            .signWith(key)
            .compact()
    }
}