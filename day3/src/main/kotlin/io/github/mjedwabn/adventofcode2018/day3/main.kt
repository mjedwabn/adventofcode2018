package io.github.mjedwabn.adventofcode2018.day3

import java.io.File

fun main(args: Array<String>) {
    println(Day3().run())
}

internal class Day3 {
    internal fun run(): Int {
        return countOverlappedSquareInchesOfFabric(ClaimParser().parseInput())
    }

    internal fun countOverlappedSquareInchesOfFabric(claims: List<Claim>): Int {
        val fabric = Fabric()
        claims.map { EuclideanClaim(it) }.forEach { claim -> fabric.addClaim(claim) }
        return fabric.getOverlappedSquaresCount()
    }
}

internal class ClaimParser {
    private val inputRegex: Regex = """#(\d+) @ (\d+),(\d+): (\d+)x(\d+)""".toRegex()

    internal fun parseInput(): List<Claim> {
        val inputPath = javaClass.classLoader.getResource("input").path
        return File(inputPath).readLines().map { parseClaim(it) }
    }

    private fun parseClaim(claim: String): Claim {
        val matchResult = inputRegex.find(claim)
        val (id, leftMargin, topMargin, width, height) = matchResult!!.destructured
        return Claim(id.toInt(), leftMargin.toInt(), topMargin.toInt(), width.toInt(), height.toInt())
    }
}

internal class EuclideanClaim(private val claim: Claim) {
    internal fun getSquares(): Sequence<Coordinate> {
        return (coord1.x..coord2.x).asSequence()
                .flatMap { x -> (coord1.y..coord2.y).asSequence()
                        .map { y -> Coordinate(x, y) } }
    }

    private val coord1 get() = Coordinate(claim.leftMargin, claim.topMargin)
    private val coord2 get() = Coordinate(claim.width + claim.leftMargin - 1, claim.height + claim.topMargin - 1)

    internal val id get() = claim.id
}

internal data class Coordinate(val x: Int, val y: Int)

internal class Fabric {
    private val squareCoverage: MutableMap<Coordinate, Int> = mutableMapOf()
    private val claims: MutableList<EuclideanClaim> = mutableListOf()

    internal fun addClaim(claim: EuclideanClaim) {
        claims += claim
        claim.getSquares().forEach { sq -> addCoverage(sq) }
    }

    private fun addCoverage(square: Coordinate) {
        squareCoverage.compute(square) { _, coverage -> if (coverage == null) 1 else coverage + 1 }
    }

    internal fun getOverlappedSquaresCount(): Int {
        return squareCoverage.filter { s -> s.value > 1 }.size
    }

    internal fun findClaimThatDoesNotOverlap(): Int? {
        return claims.filter { c -> !overlaps(c) }.map { c -> c.id }.first()
    }

    private fun overlaps(claim: EuclideanClaim): Boolean {
        return claim.getSquares().any { sq -> squareCoverage.getOrDefault(sq, 0) > 1 }
    }
}

internal data class Claim(val id: Int,
                          val leftMargin: Int, val topMargin: Int,
                          val width: Int, val height: Int)
