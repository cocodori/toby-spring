package com.tobybook.ch01

import java.sql.Connection

interface ConnectionMaker {
    fun makeConnection(): Connection
}