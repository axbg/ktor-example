package com.axbg.ctd.services

import com.axbg.ctd.models.User
import com.axbg.ctd.models.UserTO
import org.jetbrains.exposed.sql.transactions.transaction

class UserServiceImpl : UserService {
    override fun update(user: UserTO): UserTO {
        transaction {
            val userModel = User.findById(user.id)!!
            userModel.refreshHour = user.refreshHour
            userModel.notificationHour = user.notificationHour
            user.lastUpdated = userModel.lastUpdated
        }
        return user
    }

    override fun delete(userId: Long) {
        transaction {
            User.findById(userId)!!.delete()
        }
    }
}
