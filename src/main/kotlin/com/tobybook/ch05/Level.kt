package com.tobybook.ch05

enum class Level(
    val value: Int
) {
    BASIC(1),
    SILVER(2),
    GOLD(3);

    companion object {
        fun valueOf(value: Int) =
            when(value) {
                1 -> BASIC
                2 -> SILVER
                3 -> GOLD
                else -> throw IllegalArgumentException()
            }
    }
}