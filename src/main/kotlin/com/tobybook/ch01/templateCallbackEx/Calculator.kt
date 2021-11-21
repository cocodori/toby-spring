package com.tobybook.ch01.templateCallbackEx

import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException

class Calculator {
    fun fileReadTemplate(
        filepath: String,
        callback: BufferedReaderCallback
    ): Int {
        var br: BufferedReader? = null

        try {
            br = BufferedReader(FileReader(filepath))
            return callback.doSomethingWithReader(br)
        } catch (e: IOException) {
            println(e.message)
            throw e
        } finally {
            if (br != null) {
                try {
                    br.close()
                } catch (e: IOException) {
                    println(e.message)
                }
            }
        }
    }

    fun calcSum(filepath: String): Int {
        val sumCallback = object : BufferedReaderCallback {
            override fun doSomethingWithReader(br: BufferedReader): Int {
                var sum = 0
                br.readLines().forEach { sum += it.toInt() }
                return sum
            }
        }

        return fileReadTemplate(filepath, sumCallback)
    }

    fun calcMultiply(filepath: String): Int {
        val multiplyCallback = object: BufferedReaderCallback {
            override fun doSomethingWithReader(br: BufferedReader): Int {
                var multiply = 1;
                br.readLines().forEach { multiply *= it.toInt() }
                return multiply
            }
        }

        return fileReadTemplate(filepath, multiplyCallback)
    }
}