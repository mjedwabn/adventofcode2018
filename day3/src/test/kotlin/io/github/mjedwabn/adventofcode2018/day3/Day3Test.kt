package io.github.mjedwabn.adventofcode2018.day3

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day3Test {
    @Test
    internal fun sampleClaims() {
        assertEquals(4, Day3().countOverlappedSquareInchesOfFabric(listOf(
                Claim(1, 1, 3, 4, 4),
                Claim(2, 3, 1, 4, 4),
                Claim(3, 5, 5, 2, 2))))
    }

    @Test
    internal fun sampleInput() {
        assertEquals(4, Day3().run())
    }

    @Test
    internal fun claimsWithVerticalOverlappingEdge() {
        assertEquals(3, Day3().countOverlappedSquareInchesOfFabric(listOf(
                Claim(1, 1, 1, 3, 3),
                Claim(2, 3, 1, 3, 3))))
    }

    @Test
    internal fun claimsWithHorizontalOverlappingEdge() {
        assertEquals(3, Day3().countOverlappedSquareInchesOfFabric(listOf(
                Claim(1, 1, 1, 3, 3),
                Claim(2, 1, 3, 3, 3))))
    }

    @Test
    internal fun horizontallyAdjacentClaims() {
        assertEquals(0, Day3().countOverlappedSquareInchesOfFabric(listOf(
                Claim(1, 1, 1, 3, 3),
                Claim(2, 4, 1, 3, 3))))
    }

    @Test
    internal fun verticallyAdjacentClaims() {
        assertEquals(0, Day3().countOverlappedSquareInchesOfFabric(listOf(
                Claim(1, 1, 1, 3, 3),
                Claim(2, 1, 4, 3, 3))))
    }
}
