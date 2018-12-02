package io.github.mjedwabn.adventofcode2018.day2

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test


internal class Day2Test {
    @Test
    internal fun checksumOfSampleBoxIds() {
        val boxIds = listOf("abcdef", "bababc", "abbcde", "abcccd",
                "aabcdd", "abcdee", "ababab")

        assertEquals(12, Day2().checksum(boxIds))
    }

    @Test
    internal fun box1() {
        assertEquals(Pair(false, false), Day2().letterAppearsTwiceOrThreeTimes("abcdef"))
    }

    @Test
    internal fun box2() {
        assertEquals(Pair(true, true), Day2().letterAppearsTwiceOrThreeTimes("bababc"))
    }

    @Test
    internal fun box3() {
        assertEquals(Pair(true, false), Day2().letterAppearsTwiceOrThreeTimes("abbcde"))
    }

    @Test
    internal fun box4() {
        assertEquals(Pair(false, true), Day2().letterAppearsTwiceOrThreeTimes("abcccd"))
    }

    @Test
    internal fun box5() {
        assertEquals(Pair(true, false), Day2().letterAppearsTwiceOrThreeTimes("aabcdd"))
    }

    @Test
    internal fun box6() {
        assertEquals(Pair(true, false), Day2().letterAppearsTwiceOrThreeTimes("abcdee"))
    }

    @Test
    internal fun box7() {
        assertEquals(Pair(false, true), Day2().letterAppearsTwiceOrThreeTimes("ababab"))
    }
}
