package com.axbg.ctd.services

import com.axbg.ctd.models.*

class UserServiceImpl : UserService {
    override fun getUser(): User {
        return User("axbg", "parola")
    }
}