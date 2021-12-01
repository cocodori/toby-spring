package com.tobybook.ch01

import com.tobybook.ch04.UserDao
import com.tobybook.ch05.Level
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.dao.DuplicateKeyException
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.datasource.SingleConnectionDataSource

internal class UserDAOTest {
    lateinit var dao: UserDao
    lateinit var user1: User
    lateinit var user2: User
    lateinit var user3: User


    @BeforeEach
    fun setUp() {
        val dataSource = SingleConnectionDataSource(
            "jdbc:mysql://localhost/toby-testdb",
            "root",
            "",
            true
        )

        dao = UserDaoJdbc(dataSource)

        user1 = User("gyu", "규", "1234", Level.BASIC, 1, 0, "email@test.com")
        user2 = User("hoon", "훈", "1234", Level.SILVER, 55, 10, "email2@test.com")
        user3 = User("PaPa", "파파", "1234", Level.GOLD, 100, 40, "email3@test.com")
    }

    @Test
    fun update() {
        dao.deleteAll()
        dao.add(user1)
        dao.add(user2)

        user1.name = "김판호"
        user1.password = "Harry"
        user1.level = Level.GOLD
        user1.login = 1000
        user1.recommend = 9999
        user1.email = "change@mail.com"

        dao.update(user1)

        val user1Update = dao.get(user1.id)

        checkSameUser(user1, user1Update)

        val user2Same = dao.get(user2.id)
        checkSameUser(user2, user2Same)
    }

    @Test
    fun duplicateKey() {
        dao.deleteAll()

        dao.add(User("hp", "휴랫패커드", "ㅁㄴㅇㄹ"))

        val actual = assertThatThrownBy { dao.add(User("hp", "휴랫패커드", "ㅁㄴㅇㄹ")) }

        actual.isInstanceOf(DuplicateKeyException::class.java)
    }

    @Test
    fun getUserFailure() {
        dao.deleteAll()
        assertThat(dao.getCount()).isEqualTo(0)

        assertThatThrownBy { assertThat(dao.get("unknown_id")) }
            .isInstanceOf(EmptyResultDataAccessException::class.java)
    }

    @Test
    fun addAndGet() {
        dao.deleteAll()
        assertThat(dao.getCount()).isEqualTo(0)

        dao.add(user1)

        val userget1 = dao.get(user1.id)
        checkSameUser(userget1, user1)

        dao.add(user2)

        val userget2 = dao.get(user2.id)
        checkSameUser(userget2, user2)
    }

    @Test
    fun getAll() {
        dao.deleteAll()

        val users0 = dao.getAll()

        assertThat(users0.size).isEqualTo(0)

        dao.add(user1)
        val users1: List<User> = dao.getAll()
        assertThat(users1.size).isEqualTo(1)
        checkSameUser(user1, users1[0])

        dao.add(user2)
        val users2: List<User> = dao.getAll()
        assertThat(users2.size).isEqualTo(2)
        checkSameUser(user1, users2[1])
        checkSameUser(user2, users2[0])

        dao.add(user3)
        val users3: List<User> = dao.getAll()
        assertThat(users3.size).isEqualTo(3)
        checkSameUser(user1, users3[2])
        checkSameUser(user2, users3[1])
        checkSameUser(user3, users3[0])
    }

    private fun checkSameUser(user1: User, user2: User) {
        assertThat(user1.id).isEqualTo(user2.id)
        assertThat(user1.name).isEqualTo(user2.name)
        assertThat(user1.password).isEqualTo(user2.password)
        assertThat(user1.level).isEqualTo(user2.level)
        assertThat(user1.login).isEqualTo(user2.login)
        assertThat(user1.email).isEqualTo(user2.email)
    }
}