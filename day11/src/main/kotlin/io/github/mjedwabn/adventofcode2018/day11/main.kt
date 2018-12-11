package io.github.mjedwabn.adventofcode2018.day11

fun main(args: Array<String>) {
    val (x, y) = WristMountedDevice(1788).getLargestTotalPowerCell()
    println("$x,$y")
}

class WristMountedDevice(private val serialNumber: Int) {
    private val gridSize = 300

    private val grid = Array(gridSize) { IntArray(gridSize) }

    init {
        (0 until gridSize).forEach { y ->
            (0 until gridSize).forEach { x ->
                grid[y][x] = calculatePowerLevel(x + 1, y + 1)
            }
        }
    }

    private fun calculatePowerLevel(x: Int, y: Int): Int {
        val rackID = x + 10
        var powerLevel = rackID * y
        powerLevel += serialNumber
        powerLevel *= rackID
        powerLevel = getHundredsDigit(powerLevel)
        powerLevel -= 5
        return powerLevel
    }

    private fun getHundredsDigit(number: Int): Int = (number / 100) % 10

    fun getPowerLevelOfCell(x: Int, y: Int): Int = grid[y - 1][x - 1]

    fun getLargestTotalPowerCell(): Pair<Int, Int> {
        val (x1, y1, _) = getLargestPowerLevelCoordinates(3)
        return Pair(x1 + 1, y1 + 1)
    }

    private fun getLargestPowerLevelCoordinates(size: Int): LargestPowerLevelCoordinates {
        println("size $size")
        return (0..(gridSize - size)).flatMap { y ->
            (0..(gridSize - size)).map { x ->
                calculateTotalPowerLevel(x, y, size)
            }
        }.sortedByDescending { it.powerLevel }.first()
    }

    private fun calculateTotalPowerLevel(startX: Int, startY: Int, size: Int): LargestPowerLevelCoordinates {
        val totalPowerLevel = (startY until startY + size).flatMap { y ->
            (startX until startX + size).map { x -> grid[y][x] }
        }.sum()
        return LargestPowerLevelCoordinates(startX, startY, size, totalPowerLevel)
    }

    fun getLargestTotalSquareIdentifier(): SquareIdentifier {
        val (x, y, size, _) = (1..gridSize).toList().parallelStream()
                .map { getLargestPowerLevelCoordinates(it) }
                .sorted { o1, o2 -> o2.powerLevel - o1.powerLevel }
                .findFirst().orElse(LargestPowerLevelCoordinates(0, 0, 0, 0))
        return SquareIdentifier(x + 1, y + 1, size)
    }
}

data class LargestPowerLevelCoordinates(val x: Int, val y: Int, val size: Int, val powerLevel: Int)

data class SquareIdentifier(val x: Int, val y: Int, val size: Int)