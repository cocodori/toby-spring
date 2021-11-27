package com.tobybook.ch01

import com.tobybook.ch05.Level

class User(
    var id: String = "",
    var name: String = "",
    var password: String = "",
    var level: Level = Level.BASIC,
    var login: Int = 0,
    var recommend: Int = 0
)