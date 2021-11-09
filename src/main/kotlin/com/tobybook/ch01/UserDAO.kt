package com.tobybook.ch01

import java.sql.DriverManager
import java.sql.PreparedStatement

class UserDAO {
    fun add(user: User) {
        Class.forName("com.mysql.cj.jdbc.Driver")
        val c = DriverManager.getConnection("jdbc:mysql://localhost:3306/toby", "root", "")

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
        Class.forName("com.mysql.cj.jdbc.Driver")
        val c = DriverManager.getConnection("jdbc:mysql://localhost:3306/toby", "root", "")
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