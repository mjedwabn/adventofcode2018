package io.github.mjedwabn.adventofcode2018.day1

import java.io.File

fun main(args: Array<String>) {
    Day1().run()
}

class Day1 {
    fun run() {
        val inputPath = javaClass.classLoader.getResource("input").path
        val resultingFrequency = File(inputPath).readLines()
                .map { f -> f.toInt() }
                .sum()
        println(resultingFrequency)
    }
}