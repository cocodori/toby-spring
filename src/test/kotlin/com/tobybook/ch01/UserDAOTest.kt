package com.tobybook.ch01

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.springframework.context.support.ClassPathXmlApplicationContext
import org.springframework.context.support.GenericXmlApplicationContext
import org.springframework.dao.EmptyResultDataAccessException

internal class UserDAOTest {

    @Test
    fun getUserFailure() {
        val context = GenericXmlApplicationContext("/applicationContext.xml")
        val dao = context.getBean("userDAO", UserDAO::class.java)

        dao.deleteAll()
        assertThat(dao.getCount()).isEqualTo(0)

        assertThatThrownBy { assertThat(dao.get("unknown_id")) }
            .isInstanceOf(EmptyResultDataAccessException::class.java)
    }

    @Test
    fun addAndGet() {
        val context = ClassPathXmlApplicationContext("/applicationContext.xml")
        val dao = context.getBean("userDAO", UserDAO::class.java)


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