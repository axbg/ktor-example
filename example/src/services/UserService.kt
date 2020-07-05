package com.axbg.ctd.services

import com.axbg.ctd.models.UserTO

interface UserService {
    fun update(user: UserTO): UserTO
    fun delete(userId: Long)
}