package com.tobybook.ch01

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class DaoFactory {
//    @Bean
//    fun userDAO(): UserDAO = UserDAO(getConnectionMaker(),)

    fun accountDAO(): AccountDAO = AccountDAO(getConnectionMaker())

    fun MessageDAO(): MessageDAO = MessageDAO(getConnectionMaker())

    private fun getConnectionMaker() = DConnectionMaker()
}