package com.tobybook.ch01

import java.sql.Connection

class CountingConnectionMaker(
    var counter: Int = 0,
    private val realConnectionMaker: ConnectionMaker
): ConnectionMaker {

    override fun makeConnection(): Connection {
        counter++
        return realConnectionMaker.makeConnection()
    }
}