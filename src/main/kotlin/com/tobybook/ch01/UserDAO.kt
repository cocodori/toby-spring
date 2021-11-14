package com.tobybook.ch01

import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement

class UserDAO{
    lateinit var connectionMaker: ConnectionMaker
    lateinit var c: Connection
    lateinit var user: User

    fun add(user: User) {
        val c = connectionMaker.makeConnection()

        val ps: PreparedStatement = c.prepareStatement(
            "insert into users(id, name, password) values(?, ?, ?)"
        )

        ps.setString(1, user.id)
        ps.setString(2, user.name)
        ps.setString(3, user.password)

        ps.executeUpdate()

        ps.close()
        c.close()
    }

    fun get(id: String): User {
        this.c = connectionMaker.makeConnection()
        val ps = c.prepareStatement("select * from users where id = ?")

        ps.setString(1, id)

        val rs = ps.executeQuery()
        rs.next()

        val user = User(
            rs.getString("id"),
            rs.getString("name"),
            rs.getString("password")
        )

        rs.close()
        ps.close()
        c.close()

        return user
    }
}