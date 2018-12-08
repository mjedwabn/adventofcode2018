package io.github.mjedwabn.adventofcode2018.day7

import java.io.File

fun main(args: Array<String>) {
    println(Day7().run())
}

internal class Day7 {
    fun run(): String {
        return Graph(0, 1).processAndMeasure(InputParser().parse()).first
    }
}

internal class Graph(private val baseTime: Int, private val workers: Int) {
    private val steps: MutableMap<String, Step> = mutableMapOf()
    private var timer = 0

    fun processAndMeasure(instructions: Collection<Instruction>): Pair<String, Int> {
        instructions.forEach { addToGraph(it) }
        var stepsOrder = ""
        do {
            stepsOrder += doNextStep()
            ++timer
        } while (workLeftToDo())
        return Pair(stepsOrder, timer)
    }

    private fun addToGraph(instruction: Instruction) {
        val predecessor = steps.getOrPut(instruction.predecessor) { Step(instruction.predecessor.first(), baseTime) }
        val successor = steps.getOrPut(instruction.successor) { Step(instruction.successor.first(), baseTime) }

        predecessor.addSuccessor(successor)
        successor.addPredecessor(predecessor)
    }

    private fun findAvailableSteps(): List<Step> {
        return (steps.values.filter { it.processing }.sortedBy { it.name } +
                steps.values.filter { it.isAvailable }.sortedBy { it.name }).distinct()
    }

    private fun workLeftToDo(): Boolean {
        return steps.map { it.value.timeLeft }.sum() > 0
    }

    private fun doNextStep(): String {
        val steps = findAvailableSteps().take(workers)
        steps.forEach { it.process() }
        return steps.filter { !it.processing }.map { it.name }.joinToString()
    }
}

class InputParser {
    private val instructionRegex: Regex = """Step (\w) must be finished before step (\w) can begin\.""".toRegex()

    internal fun parse(): List<Instruction> {
        val inputPath = javaClass.classLoader.getResource("input").path
        return File(inputPath).readLines()
                .map { parseInstruction(it) }
    }

    private fun parseInstruction(instruction: String): Instruction {
        val instructionMatch = instructionRegex.find(instruction)
        val (predecessor, successor) = instructionMatch!!.destructured
        return Instruction(predecessor, successor)
    }
}

internal class Step(val name: Char, private val baseTime: Int) {
    private val successors: MutableList<Step> = mutableListOf()
    private val predecessors: MutableList<Step> = mutableListOf()
    private var available = true
    private var processed = false
    val isAvailable get() = available
    private val time get() = baseTime + name.toInt() - 64
    var processing = false
    var timeLeft = time

    fun addSuccessor(successor: Step) {
        successor.available = false
        successors += successor
    }

    fun process() {
        --timeLeft
        processing = true
        if (timeLeft == 0) {
            processed = true
            available = false
            processing = false
            unlockSuccessorsIfPossible()
        }
    }

    private fun unlockSuccessorsIfPossible() {
        successors.forEach { it.unlockIfPossible() }
    }

    private fun unlockIfPossible() {
        if (canBeUnlocked())
            available = true
    }

    private fun canBeUnlocked(): Boolean {
        return predecessors.all { it.processed }
    }

    override fun toString(): String {
        return "Step(name='$name', successors=${successors.map { it.name }})"
    }

    fun addPredecessor(predecessor: Step) {
        predecessors += predecessor
    }
}

data class Instruction(val predecessor: String, val successor: String)