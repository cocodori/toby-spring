package com.tobybook.ch01

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.datasource.SingleConnectionDataSource

internal class UserDAOTest {
    lateinit var dao: UserDAO

    @BeforeEach
    fun setUp() {
        val dataSource = SingleConnectionDataSource(
            "jdbc:mysql://localhost/toby-testdb",
            "root",
            "",
            true
        )

        dao = UserDAO(dataSource)
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

        val user = User("Arthur", "아떠", "pwpw")

        dao.add(user)

        val user2 = dao.get(user.id)

        assertThat(dao.getCount()).isEqualTo(1)
        assertThat(user2.name).isEqualTo(user.name)
        assertThat(user2.password).isEqualTo(user.password)
    }

    @Test
    fun getAll() {
        dao.deleteAll()

        val users0 = dao.getAll()

        assertThat(users0.size).isEqualTo(0)

        val user1 = User("1", "훈", "1234")
        val user2 = User("2", "쮀", "1234")
        val user3 = User("3", "얀", "1234")

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
    }
}