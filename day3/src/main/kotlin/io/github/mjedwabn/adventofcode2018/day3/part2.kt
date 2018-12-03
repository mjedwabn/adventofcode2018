package io.github.mjedwabn.adventofcode2018.day3

fun main(args: Array<String>) {
    println(Day3Part2().run())
}

internal class Day3Part2 {
    internal fun run(): Int? {
        return findClaimThatDoesNotOverlap(ClaimParser().parseInput())
    }

    internal fun findClaimThatDoesNotOverlap(claims: List<Claim>): Int? {
        val fabric = Fabric()
        claims.map { EuclideanClaim(it) }.forEach { claim -> fabric.addClaim(claim) }
        return fabric.findClaimThatDoesNotOverlap()
    }
}
