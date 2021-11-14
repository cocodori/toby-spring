package com.tobybook.ch01

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CountingDAOFactory {
//    @Bean
//    fun userDAO() = UserDAO(connectionMaker())

    @Bean
    fun connectionMaker(): ConnectionMaker =
        CountingConnectionMaker(realConnectionMaker = realConnectionMaker())


    @Bean
    fun realConnectionMaker(): ConnectionMaker =
        DConnectionMaker()
}