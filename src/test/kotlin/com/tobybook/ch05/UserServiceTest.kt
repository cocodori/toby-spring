package com.tobybook.ch05

import com.tobybook.ch01.User
import com.tobybook.ch04.UserDao
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration

@SpringBootTest
@ContextConfiguration(locations = ["/test-applicationContext.xml"])
internal class UserServiceTest {
    @Autowired
    lateinit var userService: UserService

    @Autowired
    lateinit var userDao: UserDao

    lateinit var users: List<User>

    @BeforeEach
    fun setUp() {
        users = listOf(
            User("boo", "부", "pw1", Level.BASIC, 49, 0),
            User("poo", "푸", "pw1", Level.BASIC, 50, 0),
            User("woo", "우", "pw1", Level.SILVER, 60, 29),
            User("zoo", "주", "pw1", Level.SILVER, 60, 30),
            User("hoo", "후", "pw1", Level.GOLD, 100, 100),
        )
    }

    @Test
    fun upgradeLevels() {
        userDao.deleteAll()

        for (user in users)
            userDao.add(user)

        userService.upgradeLevels()

        checkLevel(users[0], Level.BASIC)
        checkLevel(users[1], Level.SILVER)
        checkLevel(users[2], Level.SILVER)
        checkLevel(users[3], Level.GOLD)
        checkLevel(users[4], Level.GOLD)
    }

    private fun checkLevel(user: User, expectedLevel: Level) {
        val userUpdate = userDao.get(user.id)
        assertThat(userUpdate.level).isEqualTo(expectedLevel)
    }

}