package com.axbg.ctd.services

import com.axbg.ctd.models.UserTO

interface UserService {
    fun login(): Map<String, String>
    fun update(user: UserTO): UserTO
    fun delete(userId: Int?): Unit
}