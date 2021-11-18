package com.tobybook.ch01

import java.sql.Connection
import java.sql.PreparedStatement

interface StatementStrategy {
    fun makePreparedStatement(c: Connection): PreparedStatement
}