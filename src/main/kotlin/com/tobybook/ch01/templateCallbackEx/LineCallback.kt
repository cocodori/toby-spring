package com.tobybook.ch01.templateCallbackEx

interface LineCallback<T> {
    fun doSomethingWithLine(line: String, value: T): T
}