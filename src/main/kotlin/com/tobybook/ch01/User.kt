package com.tobybook.ch01

import com.tobybook.ch05.Level
import java.lang.IllegalArgumentException

class User(
    var id: String = "",
    var name: String = "",
    var password: String = "",
    var level: Level = Level.BASIC,
    var login: Int = 0,
    var recommend: Int = 0
) {
    fun upgradeLevel() {
        if (level.next(level) == null)
            throw IllegalArgumentException("$level 은 업그레이드가 불가능합니다.")
        level = level.next(level)!!
    }
}
