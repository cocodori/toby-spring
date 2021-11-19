package com.tobybook.ch01.strategy

import com.tobybook.ch01.StatementStrategy
import com.tobybook.ch01.User
import java.sql.Connection
import java.sql.PreparedStatement

class AddStatement(
    private var user: User
) : StatementStrategy {
    override fun makePreparedStatement(c: Connection): PreparedStatement {
        val ps = c.prepareStatement(
            "insert into users(id, name, password) values(?, ? ,?)"
        )

        ps.setString(1, user.id)
        ps.setString(2, user.name)
        ps.setString(3, user.password)


        return ps
    }
}