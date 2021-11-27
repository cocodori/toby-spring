package com.tobybook.ch01

import com.tobybook.ch04.UserDao
import com.tobybook.exception.DuplicateUserIdException
import org.springframework.dao.DuplicateKeyException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.queryForObject
import java.sql.ResultSet
import java.sql.SQLException

class UserDaoJdbc(
    private val jdbcTemplate: JdbcTemplate
): UserDao {
    private val userMapper: (rs: ResultSet, rowNum: Int) -> User? = { rs, rowNum ->
        User(
            rs.getString("id"),
            rs.getString("name"),
            rs.getString("password")
        )
    }

    override fun add(user: User) {
        try {
            jdbcTemplate.update(
                "insert into users(id, name, password) values(?, ? , ?)",
                user.id,
                user.name,
                user.password
            )
        } catch (e: DuplicateKeyException) {
            throw DuplicateUserIdException(e)
        }
    }

    override fun get(id: String): User = jdbcTemplate.queryForObject<User>(
        "select * from users where id = ?",
        arrayOf<Any>(id),
        userMapper
    )!!

    override fun deleteAll() {
        jdbcTemplate.update { con -> con.prepareStatement("delete from users") }
    }

    override fun getCount(): Int =
        jdbcTemplate.queryForObject("select count(*) from users")

    override fun getAll(): List<User> {
        return jdbcTemplate.query(
            "select * from users order by id desc", userMapper,
        )
    }
}