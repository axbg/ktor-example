package com.axbg.ctd.config

import io.ktor.auth.OAuthServerSettings
import io.ktor.http.HttpMethod

object Constants {
    const val DATABASE_URL: String = "jdbc:mysql://localhost:3306/hourglass"
    const val DATABASE_DRIVER: String = "com.mysql.cj.jdbc.Driver"
    const val DATABASE_USERNAME: String = "alex"
    const val DATABASE_PASSWORD: String = "forever"
    const val googleOauthCheck = "https://oauth2.googleapis.com/tokeninfo?id_token="
    const val privateKey = "ejrijeoirjbeiorjoigj2345o24mgbioemrobemrihmio345"
}

