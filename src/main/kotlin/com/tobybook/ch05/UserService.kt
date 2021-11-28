package com.tobybook.ch05

import com.tobybook.ch01.User
import com.tobybook.ch04.UserDao

class UserService(
    private val userDao: UserDao
) {
    fun upgradeLevels() {
        val users: List<User> = userDao.getAll()

        for (user in users) {
            var changed = false

            if (user.level == Level.BASIC && user.login >= 50) {
                user.level = Level.SILVER
                changed = true
            }
            else if (user.level == Level.SILVER && user.recommend >= 30) {
                user.level = Level.GOLD
                changed = true
            }

            if (changed)
                userDao.update(user)
        }
    }
}