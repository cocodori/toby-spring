package com.tobybook.ch06

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.test.context.ContextConfiguration

@SpringBootTest
@ContextConfiguration(locations = ["/test-applicationContext.xml"])
class MessageFactoryBeanTest {
    @Autowired
    lateinit var context: ApplicationContext

    @Test
    fun getMessageFromFactoryBean() {
        val message = context.getBean("message")
        assertThat(message).isInstanceOf(Message::class.java)
        assertThat((message as Message).text).isEqualTo("Factory Bean")
    }

    @Test
    fun getFactoryBean() {
        val factory = context.getBean("&message")
        assertThat(factory).isInstanceOf(MessageFactoryBean::class.java)
    }
}