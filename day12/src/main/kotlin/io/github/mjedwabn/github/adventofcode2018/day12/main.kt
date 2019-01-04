package io.github.mjedwabn.github.adventofcode2018.day12

fun main(args: Array<String>) {
//    println(Day12().getSumOfNumbersOfAllPotsWhichContainAPlant())
    var i = 0L;
    (0..50000000000L).forEach{i++}
    println(i)
}

internal class Day12 {
    fun getSumOfNumbersOfAllPotsWhichContainAPlant(): Int {
//        val plantation = Plantation()
//        plantation.grow(20)
//        return plantation.getSumOfNumbersOfAllPotsWhichContainAPlant()
        return 0
    }
}

internal class Plantation(private val initialState: String, private val rules: Map<String, String>) {
    private var generation: String = initialState
    private var zeroPotIndex = 0

    fun grow(generations: Long) {
        (0 until generations).forEach {
            grow()
            println(generation)
            println("g${it+1} $zeroPotIndex ${getSumOfNumbersOfAllPotsWhichContainAPlant()}")
        }
    }

    private fun grow() {
        makeGenerationBoundaries()
        var newGeneration = generation.substring(0, 2)
        (0 .. (generation.length - 5))
                .forEach {
                    val pots = generation.substring(it, it + 5)
                    if (rules.containsKey(pots)) {
                        val result = rules[pots]
                        newGeneration += result
                    } else {
                        newGeneration += "."
                    }
                }
        generation = newGeneration
    }

    private fun makeGenerationBoundaries() {
        if (generation.startsWith("....."))
        else if (generation.startsWith("...")) {
        } else if (generation.startsWith("..")) {
            generation = "." + generation
            zeroPotIndex += 1
        } else if (generation.startsWith(".")) {
            generation = ".." + generation
            zeroPotIndex += 2
        } else if (generation.startsWith("#")) {
            generation = "..." + generation
            zeroPotIndex += 3
        }

        if (generation.endsWith("...")) {
        } else if (generation.endsWith(".."))
            generation = generation + "."
        else if (generation.endsWith("."))
            generation = generation + ".."
        else if (generation.endsWith("#")) {
            generation = generation + "..."
        }
    }

    fun getSumOfNumbersOfAllPotsWhichContainAPlant(): Int {
        return generation.withIndex()
                .filter { it.value == '#' }
                .map { it.index - zeroPotIndex }
                .sum()
    }
}
