package io.github.mjedwabn.adventofcode2018.day6

import java.io.File
import kotlin.math.abs


fun main(args: Array<String>) {
    println(Day6().run())
}

internal class Day6 {
    fun run(): Int {
        val coordinates = InputParser().parse()
        return findSizeOfLargestFiniteArea(coordinates)
    }

    fun findSizeOfLargestFiniteArea(coordinates: List<Coordinate>): Int {
        val board = Board(coordinates)
        board.fill()
        board.dump()
        return board.getSizeOfLargestFiniteArea()
    }
}

internal class InputParser {
    fun parse(): List<Coordinate> {
        val inputPath = javaClass.classLoader.getResource("input").path
        return File(inputPath).readLines().map { parseLine(it) }
                .withIndex().map { Coordinate(it.value.first, it.value.second, it.index + 1) }
    }

    private fun parseLine(line: String): Pair<Int, Int> {
        val (x, y) = line.split(',', limit = 2)
        return Pair(x.trim().toInt(), y.trim().toInt())
    }
}

internal class Board(private val coordinates: List<Coordinate>) {
    private val array: Array<IntArray>
    private val width: Int
    private val height: Int
    private val boundary: Boundary
    private val cs: Map<Int, Coordinate> = coordinates.map { it.id to it }.toMap()

    init {
        boundary = getBoundary(coordinates)
        width = boundary.right - boundary.left + 1
        height = boundary.bottom - boundary.top + 1
        array = Array(height) { IntArray(width) }
        coordinates.forEach { addCoordinate(it) }
    }

    private fun getBoundary(coordinates: List<Coordinate>): Boundary {
        val sortedByX = coordinates.sortedBy { it.x }
        val sortedByY = coordinates.sortedBy { it.y }

        return Boundary(sortedByX.first().x, sortedByX.last().x,
                sortedByY.first().y, sortedByY.last().y)
    }

    private fun addCoordinate(c: Coordinate) {
        val y = c.y - boundary.top
        val x = c.x - boundary.left
        array[y][x] = c.id
    }

    fun dump() {
        (0..(height-1))
                .forEach{ y ->
                    (0..(width-1)).forEach { x -> printLocation(x, y) }
                    println("")
                }
    }

    private fun printLocation(x: Int, y: Int) {
        val field = array[y][x]
        when {
            field == Int.MAX_VALUE -> print('?')
            field > 0 -> print('A' + field%26 - 1)
            field < 0 -> print('a' + abs(field)%26 - 1)
            else -> print('.')
        }
    }

    fun fill() {
        initLocations()
        fillAreas()
    }

    private fun fillAreas() {
        (0..(height - 1))
                .forEach { y ->
                    (0..(width - 1)).forEach { x -> fillLocation(x, y) }
                }
    }

    private fun fillLocation(x: Int, y: Int) {
        val location = array[y][x]
        if (location == Int.MAX_VALUE)
            array[y][x] = calculateArea(x, y)
    }

    private fun calculateArea(x: Int, y: Int): Int {
        val firstTwo = coordinates.map { it.id to distance(it, x, y) }.sortedBy { it.second }.subList(0, 2)
        if (firstTwo[0].second != firstTwo[1].second)
            return -firstTwo[0].first
        else
            return 0
    }

    fun getSizeOfLargestFiniteArea(): Int {
        val largest = coordinates.filter { !isInfinite(it) }
                .map { it.id to area(it.id) }
                .sortedByDescending { it.second }
                .first()
        println("largest (${largest.second}) = ${'A' + largest.first%26 - 1}")
        return largest.second
    }

    private fun isInfinite(coordinate: Coordinate): Boolean {
        return getBoundaryLocations()
                .any { abs(array[it.second][it.first]) == coordinate.id }
    }

    private fun getBoundaryLocations(): List<Pair<Int, Int>> {
        val left = (0..(height - 1)).map { Pair(0, it) }
        val right = (0..(height - 1)).map { Pair(width - 1, it) }
        val top = (0..(width - 1)).map { Pair(it, 0) }
        val bottom = (0..(width - 1)).map { Pair(it, height - 1) }
        return left + right + top + bottom
    }

    private fun area(id: Int): Int {
        return (0..(height - 1)).flatMap { y -> (0..(width - 1)).map { x -> getOwner(x, y) } }
                .filter { abs(it) == id }.size
    }

    private fun getOwner(x: Int, y: Int): Int {
        return array[y][x]
    }

    private fun distance(coordinate: Coordinate, x: Int, y: Int): Int {
        val cx = coordinate.x - boundary.left
        val cy = coordinate.y - boundary.top
        return abs(cx - x) + abs(cy - y)
    }

    private fun initLocations() {
        (0..(height - 1))
                .forEach { y ->
                    (0..(width - 1)).forEach { x -> initLocation(x, y) }
                }
    }

    private fun initLocation(x: Int, y: Int) {
        val location = array[y][x]
        if (location == 0)
            array[y][x] = Int.MAX_VALUE
    }
}

internal data class Coordinate(val x: Int, val y: Int, val id: Int)

internal data class Boundary(val left: Int, val right: Int, val top: Int, val bottom: Int)