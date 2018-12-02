package io.github.mjedwabn.adventofcode2018.day2

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class Day2Part2Test {
    @Test
    internal fun sampleBoxIds() {
        val boxIDs: List<String> = listOf("abcde", "fghij", "klmno", "pqrst",
                "fguij", "axcye", "wvxyz")

        Assertions.assertEquals("fgij",
                Day2Part2().commonLettersBetweenTwoCorrectBoxIDs(boxIDs))
    }
}
