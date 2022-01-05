package com.tobybook.ch06

import org.springframework.aop.ClassFilter
import org.springframework.aop.support.NameMatchMethodPointcut
import org.springframework.util.PatternMatchUtils

class NameMatchClassMethodPointcut: NameMatchMethodPointcut() {
    fun setMappedClassName(mappedClassName: String) {
        classFilter = SimpleClassFilter(mappedClassName)
    }

    inner class SimpleClassFilter(
        private val mappedName: String
    ): ClassFilter {
        override fun matches(clazz: Class<*>): Boolean {
            return PatternMatchUtils.simpleMatch(mappedName, clazz.simpleName)
        }
    }
}