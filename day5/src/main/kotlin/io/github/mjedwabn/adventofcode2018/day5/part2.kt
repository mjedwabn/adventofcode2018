package io.github.mjedwabn.adventofcode2018.day5

import java.io.File


fun main(args: Array<String>) {
    println(Day5Part2().run())
}

internal class Day5Part2 {
    fun run(): Int = getRemainingUnits(parseInput())

    private fun parseInput(): String {
        val inputPath = javaClass.classLoader.getResource("input").path
        return File(inputPath).readLines()[0]
    }

    fun getRemainingUnits(polymer: String): Int =
            getPolarityInsensitiveUnits(polymer).parallelStream()
                    .map { polymer.replace(it.toString(), "", true) }
                    .map { react(it) }
                    .sorted()
                    .findFirst().orElse(0)

    private fun react(polymer: String): Int = Reactor().react(polymer).length

    private fun getPolarityInsensitiveUnits(polymer: String): List<Char> =
            polymer.toLowerCase().toCharArray().distinct()
}
