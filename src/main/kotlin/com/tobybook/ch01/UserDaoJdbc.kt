package com.tobybook.ch01

import com.tobybook.ch04.UserDao
import com.tobybook.ch05.Level
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.queryForObject
import java.sql.ResultSet
import javax.sql.DataSource

class UserDaoJdbc(
    private val dataSource: DataSource
): UserDao {
    var jdbcTemplate: JdbcTemplate = JdbcTemplate(dataSource)

    private val userMapper: (rs: ResultSet, rowNum: Int) -> User? = { rs, _ ->
        User(
            rs.getString("id"),
            rs.getString("name"),
            rs.getString("password"),
            Level.valueOf(rs.getInt("level")),
            rs.getInt("login"),
            rs.getInt("recommend")
        )
    }

    override fun add(user: User) {
        jdbcTemplate.update(
            "insert into users(id, name, password, Level, Login, Recommend) values(?, ?, ?, ?, ?, ?)",
            user.id,
            user.name,
            user.password,
            user.level.value,
            user.login,
            user.recommend
        )
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

    override fun update(user: User) {
        jdbcTemplate.update(
            "UPDATE users " +
                    "SET name = ?," +
                    "password = ?," +
                    "level = ?," +
                    "login = ?," +
                    "recommend = ? " +
                "WHERE id = ?",
            user.name,
            user.password,
            user.level.value,
            user.login,
            user.recommend,
            user.id
        )
    }

    override fun getAll(): List<User> {
        return jdbcTemplate.query(
            "select * from users order by id desc", userMapper,
        )
    }
}