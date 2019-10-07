package com.axbg.ctd.config

import io.ktor.auth.OAuthServerSettings
import io.ktor.http.HttpMethod

object Constants {
    const val DATABASE_URL: String = "jdbc:mysql://localhost:3306/hourglass"
    const val DATABASE_DRIVER: String = "com.mysql.cj.jdbc.Driver"
    const val DATABASE_USERNAME: String = "alex"
    const val DATABASE_PASSWORD: String = "forever"
    val googleOauthProvider = OAuthServerSettings.OAuth2ServerSettings(
        name = "google",
        authorizeUrl = "https://accounts.google.com/o/oauth2/auth",
        accessTokenUrl = "https://www.googleapis.com/oauth2/v3/token",
        requestMethod = HttpMethod.Post,

        clientId = "xxxxxxxxxxx.apps.googleusercontent.com",
        clientSecret = "yyyyyyyyyyy",
        defaultScopes = listOf("profile") // no email, but gives full name, picture, and id
    )
}

