package com.tobybook.ch05

import com.tobybook.ch01.User
import com.tobybook.ch04.UserDao
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.fail
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.mail.MailSender
import org.springframework.mail.SimpleMailMessage
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ContextConfiguration
import org.springframework.transaction.PlatformTransactionManager

@SpringBootTest
@ContextConfiguration(locations = ["/test-applicationContext.xml"])
internal class UserServiceTest {
    inner class MockMailSender(
        val request: MutableList<String> = mutableListOf()
    ): MailSender {
        override fun send(simpleMessage: SimpleMailMessage) {
            simpleMessage.to?.let { request.add(it[0]) }
        }

        override fun send(vararg simpleMessages: SimpleMailMessage?) {
            TODO("Not yet implemented")
        }

    }

    @Autowired
    lateinit var userService: UserService

    @Autowired
    lateinit var userDao: UserDao

    @Autowired
    lateinit var transactionManager: PlatformTransactionManager

    @Autowired
    lateinit var mailSender: MailSender

    lateinit var users: List<User>

    @BeforeEach
    fun setUp() {
        users = listOf(
            User("boo", "부", "pw1", Level.BASIC, MIN_LOGIN_COUNT_FOR_SILVER - 1, 0),
            User("poo", "푸", "pw1", Level.BASIC, MIN_LOGIN_COUNT_FOR_SILVER, 0),
            User("woo", "우", "pw1", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD - 1),
            User("zoo", "주", "pw1", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD),
            User("hoo", "후", "pw1", Level.GOLD, 100, Int.MAX_VALUE),
        )
    }

    @Test
    @DirtiesContext //컨텍스트의 DI 설정을 변경하는 테스트라는 것을 알려준다
    fun upgradeLevels() {
        userDao.deleteAll()

        for (user in users)
            userDao.add(user)

        val mockMailSender = MockMailSender()
        userService.mailSender = mockMailSender
        userService.upgradeLevels()

        checkLevelUpgraded(users[0], false)
        checkLevelUpgraded(users[1], true)
        checkLevelUpgraded(users[2], false)
        checkLevelUpgraded(users[3], true)
        checkLevelUpgraded(users[4], false)

        val request = mockMailSender.request
        assertThat(request.size).isEqualTo(2)
        assertThat(request[0]).isEqualTo(users[1].email)
        assertThat(request[1]).isEqualTo(users[3].email)
    }

    private fun checkLevelUpgraded(user: User, upgraded: Boolean) {
        val userUpdate = userDao.get(user.id)
        if (upgraded)
            assertThat(userUpdate.level).isEqualTo(user.level.next(user.level))
        else
            assertThat(userUpdate.level).isEqualTo(user.level)
    }

    @Test
    fun add() {
        userDao.deleteAll()

        val userWithLevel = users[4]
        val userWithoutLevel = users[0].also { it.level = Level.SILVER }

        userService.add(userWithLevel)
        userService.add(userWithoutLevel)

        val userWithLevelRead = userDao.get(userWithLevel.id)
        val userWithoutLevelRead = userDao.get(userWithoutLevel.id)

        assertThat(userWithLevelRead.level).isEqualTo(userWithLevel.level)
        assertThat(userWithoutLevelRead.level).isEqualTo(Level.BASIC)
    }

    inner class TestUserService(
        private val id: String
    ) : UserService(userDao, transactionManager, mailSender) {

        override fun upgradeLevel(user: User) {
            if (user.id == this.id) throw TestUserServiceException()
            super.upgradeLevel(user)
        }
    }

    @Test
    fun upgradeAllOrNothing() {
        val testUserService: UserService = TestUserService(users[3].id)
        userDao.deleteAll()

        for (user in users)
            userDao.add(user)

        try {
            testUserService.upgradeLevels()
            fail("TestServiceException expected")
        } catch (e: TestUserServiceException) {
            e.printStackTrace()
        }

        checkLevelUpgraded(users[1], false)
    }

}

class TestUserServiceException : RuntimeException() {

}
