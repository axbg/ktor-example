package com.axbg.ctd.config

object Constants {
    const val DATABASE_URL: String = "jdbc:mysql://localhost:3306/db"
    const val DATABASE_DRIVER: String = "com.mysql.cj.jdbc.Driver"
    const val DATABASE_USERNAME: String = "db_user"
    const val DATABASE_PASSWORD: String = "db_password"
    const val TOKEN_ENC_KEY = "SOMETHING_LONG_HARD_AND_VERY_SECRET"
    const val TOKEN_COOKIE = "X-REFRESH-TOKEN"

    const val googleOauthCheck = "https://oauth2.googleapis.com/tokeninfo?id_token="

    val publicRoutes: List<String> = listOf("/", "/login")
}

