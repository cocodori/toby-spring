package com.tobybook.ch05

import com.tobybook.ch01.User
import com.tobybook.ch04.UserDao
import com.tobybook.ch05.Level.*
import org.springframework.jdbc.datasource.DataSourceUtils
import org.springframework.transaction.support.TransactionSynchronizationManager
import javax.sql.DataSource

const val MIN_LOGIN_COUNT_FOR_SILVER = 50
const val MIN_RECOMMEND_FOR_GOLD = 30

open class UserService(
    private val userDao: UserDao,
    private val dataSource: DataSource
) {
    fun upgradeLevels() {
        TransactionSynchronizationManager.initSynchronization()
        val c = DataSourceUtils.getConnection(dataSource)
        c.autoCommit = false

        try {
            val users: List<User> = userDao.getAll().reversed()

            for (user in users) {
                if (canUpgradeLevel(user)) {
                    upgradeLevel(user)
                }
            }

            c.commit()
        } catch (e: Exception) {
            c.rollback()
            throw e
        } finally {
            DataSourceUtils.releaseConnection(c, dataSource)
            TransactionSynchronizationManager.unbindResource(dataSource)
            TransactionSynchronizationManager.clearSynchronization()
        }
    }

    open fun upgradeLevel(user: User) {
        user.upgradeLevel()
        userDao.update(user)
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