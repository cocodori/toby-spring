package com.tobybook.ch01

import com.tobybook.exception.DuplicateUserIdException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.queryForObject
import java.sql.ResultSet
import java.sql.SQLException

class UserDAO(
    private val jdbcTemplate: JdbcTemplate
) {
    private val userMapper: (rs: ResultSet, rowNum: Int) -> User? = { rs, rowNum ->
        User(
            rs.getString("id"),
            rs.getString("name"),
            rs.getString("password")
        )
    }

    fun add(user: User){
        try {
            jdbcTemplate.update(
                "insert into users(id, name, password) values(?, ? , ?)",
                user.id,
                user.name,
                user.password
            )
        } catch(e: SQLException) {
            if (e.errorCode == 1062) {
                throw DuplicateUserIdException(e)
            } else {
                throw RuntimeException(e)
            }
        }
    }

    fun get(id: String): User = jdbcTemplate.queryForObject<User>(
            "select * from users where id = ?",
            arrayOf<Any>(id),
            userMapper
        )!!

    fun deleteAll() =
        jdbcTemplate.update { con -> con.prepareStatement("delete from users") }

    fun getCount(): Int =
        jdbcTemplate.queryForObject("select count(*) from users")

    fun getAll(): List<User> {
        return jdbcTemplate.query(
            "select * from users order by id desc", userMapper,
        )
    }
}