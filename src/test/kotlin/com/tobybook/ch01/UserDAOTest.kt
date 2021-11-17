package com.tobybook.ch01

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.context.support.ClassPathXmlApplicationContext
import org.springframework.context.support.GenericXmlApplicationContext
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ContextConfiguration

@SpringBootTest
@ContextConfiguration(locations = ["/test-applicationContext.xml"])
internal class UserDAOTest {
    @Autowired
    lateinit var context: ApplicationContext
    lateinit var dao: UserDAO

    @BeforeEach
    fun setUp() {
        dao = context.getBean("userDAO", UserDAO::class.java)
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