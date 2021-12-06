package com.tobybook.ch06.proxy

interface Hello {
    fun sayHello(name: String): String
    fun sayHi(name: String): String
    fun sayThankYou(name: String): String
}

class HelloTarget: Hello {
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