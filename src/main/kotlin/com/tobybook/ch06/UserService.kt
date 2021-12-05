package com.tobybook.ch06

import com.tobybook.ch01.User

interface UserService {
    fun add(user: User)
    fun upgradeLevels()
}