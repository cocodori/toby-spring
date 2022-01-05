package com.tobybook.ch06

import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.support.DefaultTransactionDefinition
import java.lang.reflect.InvocationHandler
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method


class TransactionHandler(
    private val target: Any,
    private val transactionManager: PlatformTransactionManager,
    private val pattern: String
): InvocationHandler {
    override fun invoke(proxy: Any?, method: Method?, args: Array<out Any>?): Any? {
        return if (method != null && method.name.startsWith(pattern))
            invokeInTransaction(method, args)
        else method?.invoke(target, args)
    }

    private fun invokeInTransaction(method: Method, args: Array<out Any>?): Any? {
        val status = transactionManager.getTransaction(DefaultTransactionDefinition())
        try {
            //args를 넣으면 값을 넣은 걸로 인식하고 IllegalArgumentException:wrong number of argument 에러 남
            //https://www.examplefiles.net/cs/616573
            val ret = method.invoke(target)
            transactionManager.commit(status)
            return ret
        } catch (e: InvocationTargetException) {
            transactionManager.rollback(status)
            throw e.targetException
        }
    }
}