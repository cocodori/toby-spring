package com.tobybook.ch01

import java.sql.Connection
import java.sql.PreparedStatement

class DeleteAllStatement: StatementStrategy {
    override fun makePreparedStatement(c: Connection): PreparedStatement
        = c.prepareStatement("delete from users")
}