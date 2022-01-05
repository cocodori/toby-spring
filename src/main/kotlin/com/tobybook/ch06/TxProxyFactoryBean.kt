package com.tobybook.ch06

import org.springframework.beans.factory.FactoryBean
import org.springframework.transaction.PlatformTransactionManager
import java.lang.reflect.Proxy

class TxProxyFactoryBean(
    var target: Any,
    private val transactionManager: PlatformTransactionManager,
    private val pattern: String,
    private val serviceInterFace: Class<*>
): FactoryBean<Any> {
    override fun getObject(): Any? {
        val txHandler = TransactionHandler(target, transactionManager, pattern)
        return Proxy.newProxyInstance(
            javaClass.classLoader,
            arrayOf(serviceInterFace),
            txHandler
        )
    }

    override fun getObjectType(): Class<*> {
        return serviceInterFace
    }

    override fun isSingleton() = false
}