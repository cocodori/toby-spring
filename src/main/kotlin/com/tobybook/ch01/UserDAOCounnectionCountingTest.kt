package com.tobybook.ch01

import org.springframework.context.annotation.AnnotationConfigApplicationContext

fun main() {
    val context = AnnotationConfigApplicationContext(CountingDAOFactory::class.java)

    val dao = context.getBean("userDAO", UserDAO::class.java)

    dao.get("hoon")
    dao.get("jay")

    val ccm: CountingConnectionMaker = context.getBean("connectionMaker", CountingConnectionMaker::class.java)
    println("Connection Counter: ${ccm.counter}")
}
