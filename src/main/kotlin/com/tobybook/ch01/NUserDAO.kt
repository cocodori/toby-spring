package com.tobybook.ch01

import java.sql.Connection

class NUserDAO: UserDAO() {
    override fun getConnection(): Connection {
        TODO("Not yet implemented")
    }
}