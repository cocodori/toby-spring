package com.tobybook.ch01.templateCallbackEx

import java.io.BufferedReader

interface BufferedReaderCallback {
    fun doSomethingWithReader(br: BufferedReader): Int
}