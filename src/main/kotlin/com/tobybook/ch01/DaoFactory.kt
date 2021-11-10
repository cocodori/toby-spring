package com.tobybook.ch01

class DaoFactory {
    fun userDAO(): UserDAO = UserDAO(getConnectionMaker())

    fun accountDAO(): AccountDAO = AccountDAO(getConnectionMaker())

    fun MessageDAO(): MessageDAO = MessageDAO(getConnectionMaker())

    private fun getConnectionMaker() = DConnectionMaker()
}