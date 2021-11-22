package com.tobybook.ch01

import com.tobybook.ch01.templateCallbackEx.Calculator
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CalSumTest {
    lateinit var calculator: Calculator
    lateinit var numFilePath: String

    @BeforeEach
    fun setUp() {
        calculator = Calculator()
        numFilePath = javaClass.getResource("/numbers.txt").path
    }

    @Test
    fun sumOfNumbers() {
        assertThat(calculator.calcSum(numFilePath)).isEqualTo(10)
    }

    @Test
    fun multiplyOfNumbers() {
        assertThat(calculator.calcMultiply(numFilePath)).isEqualTo(24)
    }

    @Test
    fun concatenateStrings() {
        assertThat(calculator.concatenate(numFilePath)).isEqualTo("1234")
    }
}