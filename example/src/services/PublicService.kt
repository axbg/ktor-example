package com.axbg.ctd.services

import io.ktor.http.Cookie

interface PublicService {
    fun generateRefreshCookie(userId: Long): Cookie
}
