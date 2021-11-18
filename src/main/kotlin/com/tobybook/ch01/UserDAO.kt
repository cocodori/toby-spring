package com.tobybook.ch01

import org.springframework.dao.EmptyResultDataAccessException
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import javax.sql.DataSource

abstract class UserDAO {
    lateinit var dataSource: DataSource

    fun add(user: User) {
        var c: Connection? = null
        var ps: PreparedStatement? = null

        try {
            c = dataSource.connection
            ps = c.prepareStatement("insert into users(id, name, password) values(?, ?, ?)")

            ps.setString(1, user.id)
            ps.setString(2, user.name)
            ps.setString(3, user.password)

            ps.executeUpdate()
        } catch(e: SQLException) {
            throw e
        } finally {
            if (ps != null) { try { ps.close() } catch (e: SQLException) { } }
            if (c != null) { try { c.close() } catch (e: SQLException) { } }
        }
    }

    fun get(id: String): User {
        var c: Connection? = null
        var ps: PreparedStatement? = null
        var rs: ResultSet? = null

        try {
            c = dataSource.connection
            ps = c.prepareStatement("select * from users where id = ?")

            ps.setString(1, id)

            rs = ps.executeQuery()

            var user: User? = null

            if (rs.next()) {
                user = User(
                    rs.getString("id"),
                    rs.getString("name"),
                    rs.getString("password")
                )
            }

            ps.close()
            rs.close()
            c.close()

            return user ?: throw EmptyResultDataAccessException(1)
        } catch (e: SQLException) {
            throw e
        } finally {
            if (rs != null) { try { rs.close() } catch (e: SQLException) { } }
            if (ps != null) { try { ps.close() } catch (e: SQLException) { } }
            if (c != null) { try { c.close() } catch (e: SQLException) { } }
        }
    }

    fun deleteAll() {
        var c: Connection? = null
        var ps: PreparedStatement? = null

        try {
            c = dataSource.connection
            ps = makeStatement(c)

            ps?.executeUpdate()
        } catch (e: SQLException) {
            throw e
        } finally {
            if (ps != null) { try { ps.close() } catch (e: SQLException) { } }
            if (c != null) { try { c.close() } catch (e: SQLException) { } }
        }
    }

    fun getCount(): Int {
        var c:Connection? = null
        var ps: PreparedStatement? = null
        var rs: ResultSet? = null

        try {
            c = dataSource.connection
            ps = c.prepareStatement("select count(*) from users")

            rs = ps.executeQuery()
            rs.next()

            return rs.getInt(1)
        } catch(e: SQLException) {
            throw e
        } finally {
            if (rs != null) { try { rs.close() } catch (e: SQLException) { } }
            if (ps != null) { try { ps.close() } catch (e: SQLException) { } }
            if (c != null) { try { c.close() } catch (e: SQLException) { } }
        }
    }

    abstract fun makeStatement(c: Connection): PreparedStatement
}