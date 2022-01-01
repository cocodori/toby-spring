package com.tobybook.ch05

import com.tobybook.ch01.User
import com.tobybook.ch04.UserDao
import com.tobybook.ch06.UserService
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.fail
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentCaptor
import org.mockito.Mockito.*
import org.mockito.kotlin.anyOrNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.mail.MailSender
import org.springframework.mail.SimpleMailMessage
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ContextConfiguration
import org.springframework.transaction.PlatformTransactionManager
import java.lang.reflect.Proxy
import java.util.*

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

    inner class MockUserDao(
        var users: List<User>,
        val updated: MutableList<User> = mutableListOf()
    ): UserDao {
        override fun add(user: User) { throw UnsupportedOperationException() }
        override fun get(id: String): User { throw UnsupportedOperationException() }
        override fun deleteAll() { throw UnsupportedOperationException() }
        override fun getCount(): Int { throw UnsupportedOperationException() }

        override fun getAll(): List<User> { return users }
        override fun update(user: User) { updated.add(user) }
    }

    @Autowired
    lateinit var context: ApplicationContext

    @Autowired
    lateinit var testUserService: UserService

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
            User("poo", "푸", "pw1", Level.BASIC, MIN_LOGIN_COUNT_FOR_SILVER, 0, "hoo@email.com"),
            User("woo", "우", "pw1", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD - 1),
            User("zoo", "주", "pw1", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD, "zzz@email.com"),
            User("hoo", "후", "pw1", Level.GOLD, 100, Int.MAX_VALUE),
        )
    }

    @Test
    @DirtiesContext //컨텍스트의 DI 설정을 변경하는 테스트라는 것을 알려준다
    fun upgradeLevels() {
        val mockUserDao = MockUserDao(users)
        val mockMailSender = MockMailSender()
        val userServiceImpl = UserServiceImpl(mockUserDao, mockMailSender)

        userServiceImpl.upgradeLevels()

        val updated = mockUserDao.updated

        assertThat(updated.size).isEqualTo(2)
        checkUserAndLevel(updated[0], "poo", Level.SILVER)
        checkUserAndLevel(updated[1], "zoo", Level.GOLD)

        val request = mockMailSender.request
        assertThat(request.size).isEqualTo(2)
        assertThat(request[0]).isEqualTo(users[1].email)
        assertThat(request[1]).isEqualTo(users[3].email)
    }

    @Test
    fun mockUpgradeLevels() {
        val mockUserDao = mock(UserDao::class.java)
        val mockMailSender = mock(MailSender::class.java)

        `when`(mockUserDao.getAll()).thenReturn(this.users)

        val userServiceImpl = UserServiceImpl(mockUserDao, mockMailSender)

        userServiceImpl.upgradeLevels()

        verify(mockUserDao, times(2)).update(anyOrNull())
        assertThat(users[1].level).isEqualTo(Level.SILVER)
        assertThat(users[3].level).isEqualTo(Level.GOLD)

        val mailMessageArg: ArgumentCaptor<SimpleMailMessage> =
            ArgumentCaptor.forClass(SimpleMailMessage::class.java)

        verify(mockMailSender, times(2))
            .send(mailMessageArg.capture())
        val mailMessages = mailMessageArg.allValues
        assertThat(mailMessages[0].to?.get(0)).isEqualTo(users[1].email)
        assertThat(mailMessages[1].to?.get(0)).isEqualTo(users[3].email)
    }

    private fun checkUserAndLevel(updated: User, expectedId: String, expectedLevel: Level) {
        assertThat(updated.id).isEqualTo(expectedId)
        assertThat(updated.level).isEqualTo(expectedLevel)
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

    companion object {
        class TestUserServiceImpl(
            userDao: UserDao,
            mailSender: MailSender
        ): UserServiceImpl(userDao, mailSender) {
            override fun upgradeLevel(user: User) {
                super.upgradeLevel(user)
                if (user.id == "poo") throw TestUserServiceException()
            }
        }
    }

    @Test
    fun advisorAutoProxyCreator() {
        assertThat(testUserService).isInstanceOf(Proxy::class.java)
    }

    @Test
    fun upgradeAllOrNothing() {
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

    @Test
    fun reflectionAPITest() {
        val date = Class.forName("java.util.Date").newInstance() as Date
        println(date)


        val date2 = Class.forName("java.util.Date").getDeclaredConstructor().newInstance() as Date

        println(date2)
    }

}

class TestUserServiceException : RuntimeException() {

}
