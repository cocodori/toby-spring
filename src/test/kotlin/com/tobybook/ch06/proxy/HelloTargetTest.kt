package com.tobybook.ch06.proxy

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.lang.reflect.Proxy

internal class HelloTargetTest {
    @Test
    fun simpleProxy() {
        val hello = HelloTarget()
        assertThat(hello.sayHello("Hoon")).isEqualTo("Hello Hoon")
        assertThat(hello.sayHi("Hoon")).isEqualTo("Hi Hoon")
        assertThat(hello.sayThankYou("Hoon")).isEqualTo("Thank You Hoon")
    }

    @Test
    fun helloUppercaseProxyTest() {
        val proxiedHello = HelloUppercase(HelloTarget())

        assertThat(proxiedHello.sayHello("Hoon")).isEqualTo("HELLO HOON")
        assertThat(proxiedHello.sayHi("Hoon")).isEqualTo("HI HOON")
        assertThat(proxiedHello.sayThankYou("Hoon")).isEqualTo("THANK YOU HOON")
    }

    @Test
    fun test() {
        val proxiedHello = Proxy.newProxyInstance(
            javaClass.classLoader,
            arrayOf<Class<*>>(Hello::class.java),
            UppercaseHandler(HelloTarget())
        ) as Hello

        println(proxiedHello.sayHello("Bixby"))
    }
}