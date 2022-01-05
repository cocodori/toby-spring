package com.tobybook.ch06.proxy

import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method

interface Hello {
    fun sayHello(name: String): String
    fun sayHi(name: String): String
    fun sayThankYou(name: String): String
}

open class HelloTarget: Hello {
    override fun sayHello(name: String): String {
        return "Hello $name"
    }

    override fun sayHi(name: String): String {
        return "Hi $name"
    }

    override fun sayThankYou(name: String): String {
        return "Thank You $name"
    }
}

class UppercaseHandler(
    private val target: Any
    ): InvocationHandler {
    override fun invoke(proxy: Any?, method: Method?, args: Array<out Any>?): Any? {
        val ret = method?.invoke(target, args)

        return if (ret is String && method.name.startsWith("say"))
            ret.uppercase()
        else ret
    }
}

class HelloUppercase(
    private val hello: Hello
): Hello {
    override fun sayHello(name: String): String {
        return hello.sayHello(name).uppercase()
    }

    override fun sayHi(name: String): String {
        return hello.sayHi(name).uppercase()
    }

    override fun sayThankYou(name: String): String {
        return hello.sayThankYou(name).uppercase()
    }
}