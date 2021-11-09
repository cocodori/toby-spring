package com.tobybook.ch01

import java.sql.Connection
import java.sql.DriverManager

class SimpleConnectionMaker {
    fun makeNewConnection(): Connection {
        Class.forName("com.mysql.cj.jdbc.Driver")

        return DriverManager.getConnection("jdbc:mysql://localhost:3306/toby", "root", "")
    }


}
