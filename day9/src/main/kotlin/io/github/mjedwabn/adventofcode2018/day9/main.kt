package io.github.mjedwabn.adventofcode2018.day9

import java.io.File
import java.util.*

fun main(args: Array<String>) {
    println(Day9().run())
}

class Day9 {
    private val gameParamsRegex: Regex = """(\d+) players; last marble is worth (\d+) points""".toRegex()

    fun run(): Int {
        val inputPath = javaClass.classLoader.getResource("input").path
        val gameParams = File(inputPath).readLines()[0]
        val matchResult = gameParamsRegex.find(gameParams)
        val (numberOfPlayers, lastMarbleNumber) = matchResult!!.destructured
        return getHighScore(numberOfPlayers.toInt(), lastMarbleNumber.toInt())
    }

    fun getHighScore(numberOfPlayers: Int, lastMarbleNumber: Int): Int {
        val game = MarblesGame(numberOfPlayers, lastMarbleNumber)
        game.play()
        return game.getHighScore()
    }
}

class MarblesGame(private val numberOfPlayers: Int, private val lastMarbleNumber: Int) {
    private val players: Iterator<Int>
    private val playerScoreTable: MutableMap<Int, Int>
    private val circle: Circle = Circle()

    init {
        players = cycle((1..numberOfPlayers).toList())
        playerScoreTable = (1..numberOfPlayers).map { it to 0 }.toMap().toMutableMap()
    }

    private fun cycle(list: List<Int>): Iterator<Int> {
        var i = 0
        return generateSequence { list[i++ % list.size] }.iterator()
    }

    fun play() = (1..lastMarbleNumber).forEach { placeMarble(nextPlayer(), it) }

    private fun nextPlayer(): Int = players.next()

    private fun placeMarble(player: Int, marble: Int) {
        if (marble % 10000 == 0)
            println("${(marble * 1.0 / lastMarbleNumber) * 100}%")
        val gained = circle.placeMarble(marble)
        playerScoreTable.computeIfPresent(player) { _, s -> s + gained }
    }

    fun getHighScore(): Int = playerScoreTable.values.sortedDescending().first()
}

internal class Circle {
    private var circle: LinkedList<Int> = LinkedList(listOf(0))
    private var currentMarble = MarbleIterator(circle)

    fun placeMarble(marble: Int): Int = when {
        marble % 23 == 0 -> placeScoringMarble(marble)
        else -> placeStandardMable(marble)
    }

    private fun placeScoringMarble(marble: Int): Int {
        var total = marble
        (1..7).forEach { currentMarble.moveToPrevious() }
        total += currentMarble.get()
        currentMarble.remove()
        return total
    }

    private fun placeStandardMable(marble: Int): Int {
        currentMarble.moveToNext()
        currentMarble.add(marble)
        currentMarble.moveToNext()
        return 0
    }

    override fun toString(): String {
        return circle.withIndex()
                .map { if (it.index == currentMarble.getIndex()) "(${it.value})" else "${it.value}" }
                .joinToString(" ")
    }
}

class MarbleIterator(private val marbles: LinkedList<Int>) : MutableListIterator<Int> {
    private var index = 0

    override fun hasPrevious(): Boolean = true

    override fun nextIndex(): Int = (index + 1) % marbles.size

    fun getIndex(): Int = index

    override fun previous(): Int {
        index = if (index - 1 < 0) marbles.size - 1 else index - 1
        return marbles[index]
    }

    fun moveToPrevious() {
        index = if (index - 1 < 0) marbles.size - 1 else index - 1
    }

    override fun previousIndex(): Int = TODO("not implemented")

    override fun add(element: Int) {
        val i = nextIndex()
        marbles.add(if (i == 0) marbles.size else i, element)
    }

    override fun hasNext(): Boolean = true

    override fun next(): Int {
        index = ++index % marbles.size
        return marbles[index]
    }

    fun moveToNext() {
        index = ++index % marbles.size
    }

    fun get(): Int {
        return marbles[index]
    }

    override fun remove() {
        marbles.removeAt(index)
    }

    override fun set(element: Int): Unit = TODO("not implemented")
}