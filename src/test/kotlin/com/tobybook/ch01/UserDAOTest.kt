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
        dao = UserDAO()

        val dataSource = SingleConnectionDataSource(
            "jdbc:mysql://localhost/toby-testdb",
            "root",
            "",
            true
        )

        dao.dataSource = dataSource
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
}