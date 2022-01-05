package com.tobybook.ch06

import com.tobybook.ch01.User
import org.springframework.transaction.annotation.Transactional

interface UserService {
    fun add(user: User)
    fun deleteAll()
    fun update(user: User)
    @Transactional(readOnly = true)
    fun get(id: String): User
    @Transactional(readOnly = true)
    fun getAll(): List<User>

    fun upgradeLevels()
}