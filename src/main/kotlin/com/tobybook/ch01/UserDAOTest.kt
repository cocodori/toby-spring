package com.tobybook.ch01

import org.springframework.context.annotation.AnnotationConfigApplicationContext

fun main() {
    val context = AnnotationConfigApplicationContext(DaoFactory::class.java)
    val dao = context.getBean("userDAO", UserDAO::class.java)

    val user = User("Ian", "Moon", "pwzz")

    dao.add(user)

    println("${user.id} 등록 성공")

    val user2 = dao.get(user.id)

    println(user2.name)
    println(user2.password)
    println("${user2.name} 조회 성공")
}