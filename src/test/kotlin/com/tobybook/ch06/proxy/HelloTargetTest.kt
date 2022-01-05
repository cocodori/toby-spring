package com.tobybook.ch06.proxy

import org.aopalliance.intercept.MethodInterceptor
import org.aopalliance.intercept.MethodInvocation
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.aop.Pointcut
import org.springframework.aop.framework.ProxyFactoryBean
import org.springframework.aop.support.DefaultPointcutAdvisor
import org.springframework.aop.support.NameMatchMethodPointcut
import java.lang.reflect.Proxy

internal class HelloTargetTest {

    @Test
    fun classNamePointcutAdvisor() {
        //포인트컷 준비
        val classMethodPointcut = NameMatchMethodPointcut()

        classMethodPointcut.setClassFilter { clazz -> clazz.simpleName.startsWith("HelloT") }
        classMethodPointcut.setMappedName("sayH*")

        //테스트
        checkAdviced(HelloTarget(), classMethodPointcut, true)

        class HelloWorld: HelloTarget() {}
        checkAdviced(HelloWorld(), classMethodPointcut, false)

        class HelloToon: HelloTarget() {}
        checkAdviced(HelloToon(), classMethodPointcut, true)
    }

    private fun checkAdviced(target: Any, pointcut: Pointcut, adviced: Boolean) {
        val proxyFactoryBean = ProxyFactoryBean()
        proxyFactoryBean.setTarget(target)
        proxyFactoryBean.addAdvisor(DefaultPointcutAdvisor(pointcut, UppercaseAdvice()))
        val proxiedHello = proxyFactoryBean.`object` as Hello

        if (adviced) {
            assertThat(proxiedHello.sayHello("Hoon")).isEqualTo("HELLO HOON")
            assertThat(proxiedHello.sayHi("Hoon")).isEqualTo("HI HOON")
            assertThat(proxiedHello.sayThankYou("Hoon")).isEqualTo("Thank You Hoon")
        } else {
            assertThat(proxiedHello.sayHello("Hoon")).isEqualTo("Hello Hoon")
            assertThat(proxiedHello.sayHi("Hoon")).isEqualTo("Hi Hoon")
            assertThat(proxiedHello.sayThankYou("Hoon")).isEqualTo("Thank You Hoon")
        }
    }

    @Test
    fun pointcutAdvisor() {
        val proxyFactoryBean = ProxyFactoryBean()
        proxyFactoryBean.setTarget(HelloTarget())

        val pointcut = NameMatchMethodPointcut()
        pointcut.setMappedName("sayH*") //이름 비교조건. sayH로 시작하는 모든 메소드

        //포인트컷과 어드바이스를 Advisor로 묶어서 한 번에 추가
        proxyFactoryBean.addAdvisor(DefaultPointcutAdvisor(pointcut, UppercaseAdvice()))

        val proxiedHello = proxyFactoryBean.`object` as Hello

        assertThat(proxiedHello.sayHello("borin")).isEqualTo("HELLO BORIN")
        assertThat(proxiedHello.sayHi("borin")).isEqualTo("HI BORIN")
        assertThat(proxiedHello.sayThankYou("Borin")).isEqualTo("Thank You Borin")
    }

    @Test
    fun proxyFactoryBean() {
        val proxyFactoryBean = ProxyFactoryBean()
        proxyFactoryBean.setTarget(HelloTarget())
        proxyFactoryBean.addAdvice(UppercaseAdvice())

        val proxiedHello = proxyFactoryBean.`object` as Hello

        assertThat(proxiedHello.sayHello("Hoon")).isEqualTo("HELLO HOON")
        assertThat(proxiedHello.sayHi("hoon")).isEqualTo("HI HOON")
        assertThat(proxiedHello.sayThankYou("hooN")).isEqualTo("THANK YOU HOON")
    }

    inner class UppercaseAdvice: MethodInterceptor {
        override fun invoke(invocation: MethodInvocation): Any? {
            val ret: String = invocation.proceed() as String
            return ret.uppercase()
        }
    }

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