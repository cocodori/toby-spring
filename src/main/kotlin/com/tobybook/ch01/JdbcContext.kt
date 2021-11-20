package com.tobybook.ch01

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.SQLException
import javax.sql.DataSource

class JdbcContext(
    var dataSource: DataSource
){
    fun executeSql(query: String) {
        workWithStatementStrategy(
            object : StatementStrategy {
                override fun makePreparedStatement(c: Connection): PreparedStatement =
                    c.prepareStatement(query)
            }
        )
    }

    fun workWithStatementStrategy(stmt: StatementStrategy) {
        var c: Connection? = null
        var ps: PreparedStatement? = null

        try {
            c = this.dataSource.getConnection()
            ps = stmt.makePreparedStatement(c)

            ps.executeUpdate()
        } catch(e: SQLException) {
            throw e
        } finally {
            if (ps != null) { try { ps.close() } catch (e: SQLException) {} }
            if (c != null) { try { c.close() } catch (e: SQLException) {} }
        }
    }
}