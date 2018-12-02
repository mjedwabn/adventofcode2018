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

    fun commonLettersBetweenTwoCorrectBoxIDs(boxIDs: List<String>): String =
            generatePairs(boxIDs)
                    .filter { boxesAreCorrect(it.first, it.second) }
                    .map { commonLetters(it.first, it.second) }
                    .first()

    private fun boxesAreCorrect(box1: String, box2: String): Boolean =
            box1.indices.any { boxesAreCorrectAtPosition(box1, box2, it) }

    private fun commonLetters(box1: String, box2: String): String = box1.indices
            .filter { boxesAreCorrectAtPosition(box1, box2, it) }
            .map { box1.removeRange(it, it + 1) }
            .first()

    private fun boxesAreCorrectAtPosition(box1: String, box2: String, pos: Int) =
            differAtPosition(box1, box2, pos) && otherLettersAreSame(box1, box2, pos)

    private fun differAtPosition(box1: String, box2: String, pos: Int) =
            box1[pos] != box2[pos]

    private fun otherLettersAreSame(box1: String, box2: String, pos: Int) =
            box1.removeRange(pos, pos + 1) == box2.removeRange(pos, pos + 1)

    private fun generatePairs(boxIDs: List<String>): Sequence<Pair<String, String>> =
            boxIDs.asSequence()
                    .flatMap { box1 -> boxIDs.asSequence().map { box2 -> Pair(box1, box2) } }
                    .filter { pair -> pair.first != pair.second }
}
