package io.github.mjedwabn.adventofcode2018.day3

import java.io.File

fun main(args: Array<String>) {
    println(Day3().run())
}

internal class Day3 {
    private val inputRegex: Regex = """#(\d+) @ (\d+),(\d+): (\d+)x(\d+)""".toRegex()

    fun run(): Int {
        val inputPath = javaClass.classLoader.getResource("input").path
        val claims = File(inputPath).readLines().map { parseClaim(it) }
        return countOverlappedSquareInchesOfFabric(claims)
    }

    private fun parseClaim(claim: String): Claim {
        val matchResult = inputRegex.find(claim)
        val (id, leftMargin, topMargin, width, height) = matchResult!!.destructured
        return Claim(id.toInt(), leftMargin.toInt(), topMargin.toInt(), width.toInt(), height.toInt())
    }

    fun countOverlappedSquareInchesOfFabric(claims: List<Claim>): Int {
        val fabric = Fabric()
        claims.map { EuclideanClaim(it) }.forEach { claim -> fabric.addClaim(claim) }
        return fabric.getOverlappedSquaresCount()
    }
}

internal class EuclideanClaim(private val claim: Claim) {
    fun getSquares(): Sequence<Coordinate> {
        return (coord1.x..coord2.x).asSequence()
                .flatMap { x -> (coord1.y..coord2.y).asSequence()
                        .map { y -> Coordinate(x, y) } }
    }

    private val coord1 get() = Coordinate(claim.leftMargin, claim.topMargin)
    private val coord2 get() = Coordinate(claim.width + claim.leftMargin - 1, claim.height + claim.topMargin - 1)
}

internal data class Coordinate(val x: Int, val y: Int)

internal class Fabric {
    private val squareCoverage: MutableMap<Coordinate, Int> = mutableMapOf();

    fun addClaim(claim: EuclideanClaim) {
        claim.getSquares().forEach { sq -> addCoverage(sq) }
    }

    private fun addCoverage(square: Coordinate) {
        squareCoverage.compute(square) { _, coverage -> if (coverage == null) 1 else coverage + 1 }
    }

    fun getOverlappedSquaresCount(): Int {
        return squareCoverage.filter { s -> s.value > 1 }.size
    }
}

internal data class Claim(val id: Int,
                          val leftMargin: Int, val topMargin: Int,
                          val width: Int, val height: Int)
