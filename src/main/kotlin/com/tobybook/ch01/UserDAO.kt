package com.tobybook.ch01

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.queryForObject
import javax.sql.DataSource

class UserDAO(var dataSource: DataSource) {

    var jdbcTemplate: JdbcTemplate = JdbcTemplate(dataSource)

    fun add(user: User) =
        jdbcTemplate.update(
            "insert into users(id, name, password) values(?, ? , ?)",
            user.id,
            user.name,
            user.password
        )

    fun get(id: String): User = jdbcTemplate.queryForObject<User>(
            "select * from users where id = ?",
            arrayOf<Any>(id)
        ) { rs, rowNum ->
            User(
                rs.getString("id"),
                rs.getString("name"),
                rs.getString("password")
            )
        }!!

    fun deleteAll() =
        jdbcTemplate.update { con -> con.prepareStatement("delete from users") }

    fun getCount(): Int =
        jdbcTemplate.queryForObject("select count(*) from users")

    fun getAll(): List<User> = jdbcTemplate.query(
            "select * from users order by id desc",
        ) { rs, rowNum ->
            User(
                rs.getString("id"),
                rs.getString("name"),
                rs.getString("password")
            )
        }
}