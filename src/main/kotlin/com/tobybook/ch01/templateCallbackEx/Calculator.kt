package com.tobybook.ch01.templateCallbackEx

import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException

class Calculator {

    fun <T> lineReadTemplate(
        filepath: String,
        callback: LineCallback<T>,
        initVal: T
    ): T {
        var br: BufferedReader? = null

        return try {
            br = BufferedReader(FileReader(filepath))
            var res: T = initVal
            br.readLines().forEach { res = callback.doSomethingWithLine(it, res) }
            return res
        } catch (e: IOException) {
            throw e
        } finally {

        }
    }

    fun concatenate(filepath: String): String {
        val concatnateCallback = object: LineCallback<String> {
            override fun doSomethingWithLine(line: String, value: String): String {
                return value + line
            }
        }

        return lineReadTemplate(filepath, concatnateCallback, "")
    }

    fun calcSum(filepath: String): Int {
        val sumCallback = object : LineCallback<Int> {
            override fun doSomethingWithLine(line: String, value: Int): Int {
                return value + line.toInt()
            }
        }
        return lineReadTemplate(filepath, sumCallback, 0)
    }

    fun calcMultiply(filepath: String): Int {
        val multiplyCallback = object: LineCallback<Int> {
            override fun doSomethingWithLine(line: String, value: Int): Int {
                return value * line.toInt()
            }
        }

        return lineReadTemplate(filepath, multiplyCallback, 1)
    }
}