package io.github.mjedwabn.adventofcode2018.day5

import java.io.File


fun main(args: Array<String>) {
    println(Day5().run())
}

internal class Day5 {
    fun run(): Int = getRemainingUnits(parseInput())

    private fun parseInput(): String {
        val inputPath = javaClass.classLoader.getResource("input").path
        return File(inputPath).readLines()[0]
    }

    fun getRemainingUnits(polymer: String): Int =
            Reactor().react(polymer).length
}

internal class Reactor {
    fun react(polymer: String): String {
        var before: String
        var ret = polymer
        do {
            before = ret
            val after = reactOnce(before)
            ret = after
        } while (after != before)
        return ret
    }

    private fun reactOnce(polymer: String): String = when {
        polymer.length <= 1 -> polymer
        polymer.length == 2 -> destroy(polymer)
        else -> {
            val adjacentUnits = polymer.substring(0, polymer.length - 1).withIndex()
                    .map { it.value + polymer[it.index + 1].toString() }
            val index = adjacentUnits.withIndex().firstOrNull { canBeDestroyed(it.value) }
            if (index == null) polymer else polymer.substring(0, index.index) + destroy(adjacentUnits[index.index]) + polymer.substring(index.index + 2)
        }
    }

    private fun destroy(adjacentUnits: String): String =
            if (canBeDestroyed(adjacentUnits)) "" else adjacentUnits

    private fun canBeDestroyed(adjacentUnits: String): Boolean =
            Math.abs(adjacentUnits[0] - adjacentUnits[1]) == 32
}
