package com.tobybook.ch06

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.lang.reflect.Method

class ReflectionTest {
    @Test
    fun invokeMethod() {
        val name = "Spring"

        //legnth()
        assertThat(name.length).isEqualTo(6)

        val lengthMethod: Method = String::class.java.getMethod("length")
        assertThat(lengthMethod.invoke(name)).isEqualTo(6)

        //charAt()
        assertThat(name.toCharArray()[0]).isEqualTo('S')

        val charAtMethod = String::class.java.getMethod("charAt", Int::class.java)
        assertThat(charAtMethod.invoke(name, 0)).isEqualTo('S')
    }
}