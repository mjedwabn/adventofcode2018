package io.github.mjedwabn.adventofcode2018.day3

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class Day3Part2Test {
    @Test
    internal fun sampleClaims() {
        Assertions.assertEquals(3, Day3Part2().findClaimThatDoesNotOverlap(listOf(
                Claim(1, 1, 3, 4, 4),
                Claim(2, 3, 1, 4, 4),
                Claim(3, 5, 5, 2, 2))))
    }
}
