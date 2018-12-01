package io.github.mjedwabn.adventofcode2018.day1

import java.io.File
import java.time.Duration
import java.time.Instant

fun main(args: Array<String>) {
    Day1Part2().run()
}

class Day1Part2 {
    fun run() {
        val start = Instant.now()
        val inputPath = javaClass.classLoader.getResource("input").path
        val frequencies = File(inputPath).readLines()
                .map { f -> f.toInt() }
        val resultingFrequency = findFrequencyReachedTwice(frequencies)
        println(resultingFrequency)
        val end = Instant.now()
        println(Duration.between(start, end))
    }

    fun findFrequencyReachedTwice(frequencies: List<Int>): Int {
        val sums: MutableList<Int> = mutableListOf()
        var sum = 0
        sums.add(sum)
        var ret = 0
        for (frequency in cycle(frequencies)) {
            sum += frequency
            if (sums.contains(sum)) {
                ret = sum
                break
            }
            sums.add(sum)
        }
        return ret
    }

    private fun cycle(list: List<Int>): Sequence<Int> {
        var i = 0
        return generateSequence { list[i++ % list.size] }
    }
}
