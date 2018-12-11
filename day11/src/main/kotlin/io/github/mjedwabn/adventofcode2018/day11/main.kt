package io.github.mjedwabn.adventofcode2018.day11

fun main(args: Array<String>) {
    val (x, y) = WristMountedDevice(1788).getLargestTotalPowerCell()
    println("$x,$y")
}

class WristMountedDevice(private val serialNumber: Int) {
    private val grid = Array(300) { IntArray(300) }

    init {
        (0 until 300).forEach { y ->
            (0 until 300).forEach { x ->
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

    private fun getHundredsDigit(number: Int): Int {
        return (number / 100) % 10
    }

    fun getPowerLevelOfCell(x: Int, y: Int): Int {
        return grid[y - 1][x - 1]
    }

    fun getLargestTotalPowerCell(): Pair<Int, Int> {
        val (x1, y1, _) = (0 until 300 - 2).flatMap { y ->
            (0 until 300 - 2).map { x ->
                calculateTotalPowerLevel(x, y)
            }
        }
                .sortedByDescending { it.powerLevel }
                .first()
        return Pair(x1 + 1, y1 + 1)
    }

    private fun calculateTotalPowerLevel(startX: Int, startY: Int): TotalPowerLevelCoordinates {
        val totalPowerLevel = (startY until startY + 3).flatMap { y ->
            (startX until startX + 3).map { x -> grid[y][x] }
        }.sum()
        return TotalPowerLevelCoordinates(startX, startY, totalPowerLevel)
    }
}

data class TotalPowerLevelCoordinates(val x: Int, val y: Int, val powerLevel: Int)