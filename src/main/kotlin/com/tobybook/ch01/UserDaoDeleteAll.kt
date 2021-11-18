package com.tobybook.ch01

import java.sql.Connection
import java.sql.PreparedStatement

class UserDaoDeleteAll: UserDAO() {
    override fun makeStatement(c: Connection): PreparedStatement
        = c.prepareStatement("delete from users")
}