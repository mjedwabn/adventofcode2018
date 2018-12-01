package io.github.mjedwabn.adventofcode2018.day1

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day1Part2Test {
    @Test
    internal fun name() {
        assertEquals(0, Day1Part2().findFrequencyReachedTwice(listOf(1, -1)))
        assertEquals(10, Day1Part2().findFrequencyReachedTwice(listOf(3, 3, 4, -2, -4)))
        assertEquals(5, Day1Part2().findFrequencyReachedTwice(listOf(-6, 3, 8, 5, -6)))
        assertEquals(14, Day1Part2().findFrequencyReachedTwice(listOf(7, 7, -2, -7, -4)))
    }
}