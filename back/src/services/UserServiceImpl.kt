package com.axbg.ctd.services

import com.axbg.ctd.models.UserTO

class UserServiceImpl : UserService {
    override fun update(user: UserTO): UserTO {
        return user
    }

    override fun delete(userId: Long) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}