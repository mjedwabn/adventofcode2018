package io.github.mjedwabn.adventofcode2018.day2

import java.io.File


fun main(args: Array<String>) {
    Day2().run()
}

class Day2 {
    fun run() {
        val inputPath = javaClass.classLoader.getResource("input").path
        val boxIds = File(inputPath).readLines()
        println(checksum(boxIds))
    }

    fun checksum(boxIds: List<String>): Int {
        val (twice, threeTimes) = boxIds.map { id -> letterAppearsTwiceOrThreeTimes(id) }
                .map { a -> Pair(if (a.first) 1 else 0, if (a.second) 1 else 0) }
                .reduce { p1, p2 -> Pair(p1.first + p2.first, p1.second + p2.second) }
        return twice * threeTimes
    }

    fun letterAppearsTwiceOrThreeTimes(boxId: String): Pair<Boolean, Boolean> {
        val appearances = boxId.toList().groupBy { c -> c }.map { e -> e.value.size }
        return Pair(appearances.contains(2), appearances.contains(3))
    }
}
