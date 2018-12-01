package io.github.mjedwabn.adventofcode2018.day1

import java.io.File

fun main(args: Array<String>) {
    Day1Part2().run()
}

class Day1Part2 {
    fun run() {
        val inputPath = javaClass.classLoader.getResource("input").path
        val frequencies = File(inputPath).readLines()
                .map { f -> f.toInt() }
        val resultingFrequency = process(frequencies)
        println(resultingFrequency)
    }

    fun process(frequencies: List<Int>): Int {
        var sums: List<Int> = ArrayList()
        var sum: Int = 0
        sums = sums.plus(sum)
        var ret: Int = 0
        for (frequency in this.cycle(frequencies).iterator()) {
            sum += frequency
            if (sums.contains(sum)) {
                ret = sum
                break
            }
            sums = sums.plus(sum)
        }
        return ret
    }

    fun cycle(xs: List<Int>): Sequence<Int> {
        var i = 0
        return generateSequence { xs.get(i++ % xs.size) }
    }
}
