package com.tobybook.ch01

import java.sql.Connection
import java.sql.DriverManager

class DConnectionMaker: ConnectionMaker {
    override fun makeConnection(): Connection {
        Class.forName("com.mysql.cj.jdbc.Driver")

        return DriverManager.getConnection("jdbc:mysql://localhost:3306/toby", "root", "")
    }
}