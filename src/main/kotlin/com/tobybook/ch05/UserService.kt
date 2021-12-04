package com.tobybook.ch05

import com.tobybook.ch01.User
import com.tobybook.ch04.UserDao
import com.tobybook.ch05.Level.*
import org.springframework.mail.MailSender
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSenderImpl
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.TransactionStatus
import org.springframework.transaction.support.DefaultTransactionDefinition

const val MIN_LOGIN_COUNT_FOR_SILVER = 50
const val MIN_RECOMMEND_FOR_GOLD = 30

open class UserService(
    private val userDao: UserDao,
    private val transactionManager: PlatformTransactionManager,
    var mailSender: MailSender
) {
    fun upgradeLevels() {
        val status: TransactionStatus =
            transactionManager.getTransaction(DefaultTransactionDefinition())

        try {
            val users: List<User> = userDao.getAll().reversed()

            for (user in users) {
                if (canUpgradeLevel(user)) {
                    upgradeLevel(user)
                }
            }

            transactionManager.commit(status)
        } catch (e: Exception) {
            transactionManager.rollback(status)
            throw e
        }
    }

    open fun upgradeLevel(user: User) {
        user.upgradeLevel()
        userDao.update(user)
        sendUpgradeEmail(user)
    }

    private fun sendUpgradeEmail(user: User) {
        val mailMessage = SimpleMailMessage()
        mailMessage.setTo(user.email)
        mailMessage.setFrom("useradmin@ksug.org")
        mailMessage.setSubject("Upgrade 안내")
        mailMessage.setText("사용자님 등급이 ${user.level.name}")

        mailSender.send(mailMessage)
    }

    private fun canUpgradeLevel(user: User): Boolean {
        return when (user.level) {
            BASIC -> user.login >= MIN_LOGIN_COUNT_FOR_SILVER
            SILVER -> user.recommend >= MIN_RECOMMEND_FOR_GOLD
            GOLD -> false
        }
    }

    fun add(user: User) {
        userDao.add(user.also { it.level = BASIC })
    }
}