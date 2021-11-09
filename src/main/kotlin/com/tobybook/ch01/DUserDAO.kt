package com.tobybook.ch01

import java.sql.Connection

class DUserDAO: UserDAO() {
    override fun getConnection(): Connection {
        TODO("Not yet implemented")
    }
}