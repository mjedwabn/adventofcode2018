package io.github.mjedwabn.adventofcode2018.day2

import java.io.File


fun main(args: Array<String>) {
    Day2Part2().run()
}

class Day2Part2 {
    fun run() {
        val inputPath = javaClass.classLoader.getResource("input").path
        val boxIds = File(inputPath).readLines()
        println(commonLettersBetweenTwoCorrectBoxIDs(boxIds))
    }

    fun commonLettersBetweenTwoCorrectBoxIDs(boxIDs: List<String>): String {
        val pairs = generatePairs(boxIDs)
        for (pair in pairs) {
            if (boxesAreCorrect(pair.first, pair.second))
                return commonLetters(pair.first, pair.second)
        }
        return ":("
    }

    private fun boxesAreCorrect(box1: String, box2: String): Boolean {
        for (i in 0..(box1.length - 1)) {
            if (box1[i] != box2[i] && box1.removeRange(i, i + 1) == box2.removeRange(i, i + 1))
                return true
        }
        return false
    }

    private fun commonLetters(box1: String, box2: String): String {
        for (i in 0..(box1.length - 1)) {
            if (box1[i] != box2[i] && box1.removeRange(i, i + 1) == box2.removeRange(i, i + 1))
                return box1.removeRange(i, i + 1)
        }
        return box1
    }

    private fun generatePairs(boxIDs: List<String>): Sequence<Pair<String, String>> {
        return boxIDs.asSequence()
                .flatMap { box1 -> boxIDs.asSequence().map { box2 -> Pair(box1, box2) } }
                .filter { pair -> pair.first != pair.second }
    }
}
