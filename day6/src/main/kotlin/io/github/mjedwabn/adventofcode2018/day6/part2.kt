package io.github.mjedwabn.adventofcode2018.day6


fun main(args: Array<String>) {
    println(Day6Part2().run(10000))
}

class Day6Part2 {
    fun run(distance: Int): Int {
        val coordinates = InputParser().parse()
        return getSizeOfRegion(coordinates, distance)
    }

    private fun getSizeOfRegion(coordinates: List<Coordinate>, distance: Int): Int {
        val board = Board(coordinates)
        board.fill()
        return board.getSizeOfRegion(distance)
    }
}
