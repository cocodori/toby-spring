package com.tobybook.ch01

import org.springframework.context.support.GenericXmlApplicationContext

fun main() {
    val context = GenericXmlApplicationContext("/applicationContext.xml")
    val dao = context.getBean("userDAO", UserDaoJdbc::class.java)

    val user = dao.get("hoon")

    println(user.name)
    println(user.password)
    println("${user.name} 조회 성공")
}