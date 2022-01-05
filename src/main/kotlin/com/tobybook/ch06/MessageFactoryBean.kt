package com.tobybook.ch06

import org.springframework.beans.factory.FactoryBean

class MessageFactoryBean(
    var text: String
    ): FactoryBean<Message> {
    override fun getObject(): Message {
        return Message.newMessage(this.text)
    }

    override fun getObjectType(): Class<*> {
        return Message.javaClass
    }

    override fun isSingleton() = false
}