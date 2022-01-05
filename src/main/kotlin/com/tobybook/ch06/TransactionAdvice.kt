package com.tobybook.ch06

import org.aopalliance.intercept.MethodInterceptor
import org.aopalliance.intercept.MethodInvocation
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.support.DefaultTransactionDefinition
import java.lang.RuntimeException

class TransactionAdvice(
    private val transactionManager: PlatformTransactionManager
): MethodInterceptor {
    override fun invoke(invocation: MethodInvocation): Any? {
        val status =
            transactionManager.getTransaction(DefaultTransactionDefinition())

        return try {
            val ret = invocation.proceed()
            transactionManager.commit(status)
            ret
        } catch (e: RuntimeException) {
            transactionManager.rollback(status)
            throw e
        }
    }
}