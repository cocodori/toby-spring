package com.tobybook.ch06

import com.tobybook.ch01.User
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.support.DefaultTransactionDefinition
import java.lang.RuntimeException

class UserServiceTx(
    private val userService: UserService,
    private val transactionManager: PlatformTransactionManager
): UserService {
    override fun add(user: User) {
        userService.add(user)
    }

    override fun upgradeLevels() {
        val status = transactionManager.getTransaction(DefaultTransactionDefinition())

        try {
            userService.upgradeLevels()
            transactionManager.commit(status)
        } catch (e: RuntimeException) {
            transactionManager.rollback(status)
            throw e
        }
    }
}