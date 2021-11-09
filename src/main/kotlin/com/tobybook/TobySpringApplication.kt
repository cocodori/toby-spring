package com.tobybook

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TobySpringApplication

fun main(args: Array<String>) {
	runApplication<TobySpringApplication>(*args)
}
