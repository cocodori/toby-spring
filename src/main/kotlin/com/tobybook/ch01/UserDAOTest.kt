package com.tobybook.ch01

fun main() {
    val dao = UserDAO(DConnectionMaker())

    val user = User("Jay", "제이", "pwzz")

    dao.add(user)

    println("${user.id} 등록 성공")

    val user2 = dao.get(user.id)

    println(user2.name)
    println(user2.password)
    println("${user2.name} 조회 성공")
}