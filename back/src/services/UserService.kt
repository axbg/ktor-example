package com.axbg.ctd.services

interface UserService {
    fun login(): Map<String, String>
    fun update(refreshHour: String?, notificationHour: String?): Unit
    fun delete(userId: Int?): Unit
}