package io.github.mjedwabn.adventofcode2018.day4

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

internal class Day4Test {
    @Test
    internal fun sampleRecords() {
        val records: List<Record> = listOf(
                Record(LocalDateTime.of(1518, 11, 1, 0, 5), 10, GuardEvent.FallAsleep),
                Record(LocalDateTime.of(1518, 11, 1, 0, 25), 10, GuardEvent.Awakening),
                Record(LocalDateTime.of(1518, 11, 1, 0, 30), 10, GuardEvent.FallAsleep),
                Record(LocalDateTime.of(1518, 11, 1, 0, 55), 10, GuardEvent.Awakening),
                Record(LocalDateTime.of(1518, 11, 2, 0, 40), 99, GuardEvent.FallAsleep),
                Record(LocalDateTime.of(1518, 11, 2, 0, 50), 99, GuardEvent.Awakening),
                Record(LocalDateTime.of(1518, 11, 3, 0, 24), 10, GuardEvent.FallAsleep),
                Record(LocalDateTime.of(1518, 11, 3, 0, 29), 10, GuardEvent.Awakening),
                Record(LocalDateTime.of(1518, 11, 4, 0, 36), 99, GuardEvent.FallAsleep),
                Record(LocalDateTime.of(1518, 11, 4, 0, 46), 99, GuardEvent.Awakening),
                Record(LocalDateTime.of(1518, 11, 5, 0, 45), 99, GuardEvent.FallAsleep),
                Record(LocalDateTime.of(1518, 11, 5, 0, 55), 99, GuardEvent.Awakening)
        )
        assertEquals(240, Day4().strategy1(records))
    }

    @Test
    internal fun sampleInput() {
        assertEquals(240, Day4().run())
    }
}
