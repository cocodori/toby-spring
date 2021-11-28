package com.tobybook.ch01

import com.tobybook.ch05.Level
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.lang.IllegalArgumentException
import kotlin.math.log

internal class UserTest {
    lateinit var user: User

    @BeforeEach
    fun setUp() {
        user = User(
            id = "hoon",
            name = "훈",
            password = "pw1",
            level = Level.BASIC,
            login = 10,
            recommend = 5
        )
    }

    @Test
    fun upgradeLevel() {
        val levels = Level.values()

        for (level in levels) {
            if (level.next(level) == null) continue
            user.level = level
            user.upgradeLevel()
            assertThat(user.level).isEqualTo(level.next(level))
        }
    }

    @Test
    fun cannotUpgradeLevel() {
        val levels = Level.values()

        val actual = assertThatThrownBy {
            for (level in levels) {
                if (level.next(level) != null) continue
                user.level = level
                user.upgradeLevel()
            }
        }

        actual.isInstanceOf(IllegalArgumentException::class.java)
    }
}