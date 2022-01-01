package com.tobybook.ch06

import com.tobybook.ch01.User

interface UserService {
    fun add(user: User)
    fun get(id: String): User
    fun getAll(): List<User>
    fun deleteAll()
    fun update(user: User)

    fun upgradeLevels()
}