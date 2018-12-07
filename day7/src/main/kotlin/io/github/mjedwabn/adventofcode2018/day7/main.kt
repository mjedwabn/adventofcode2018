package io.github.mjedwabn.adventofcode2018.day7

import java.io.File

fun main(args: Array<String>) {
    println(Day7().run())
}

internal class Day7 {
    private val steps: MutableMap<String, Step> = mutableMapOf()
    fun run(): String {
        return getStepsOrder(InputParser().parse())
    }

    fun getStepsOrder(instructions: Collection<Instruction>): String {
        instructions.forEach { addToGraph(it) }
        var stepsOrder = ""
        do {
            stepsOrder += doNextStep()
        } while(stepsLeftToDo())
        return stepsOrder
    }

    private fun stepsLeftToDo(): Boolean {
        return !findAvailableSteps().isEmpty()
    }

    private fun doNextStep(): String {
        val step = findAvailableSteps().first()
        step.process()
        return step.name
    }

    private fun findAvailableSteps(): List<Step> {
        return steps.values.filter { it.isAvailable }.sortedBy { it.name }
    }

    private fun addToGraph(instruction: Instruction) {
        val predecessor = steps.getOrPut(instruction.predecessor) { Step(instruction.predecessor) }
        val successor = steps.getOrPut(instruction.successor) { Step(instruction.successor) }

        predecessor.addSuccessor(successor)
        successor.addPredecessor(predecessor)
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


internal class Step(val name: String) {
    private val successors: MutableList<Step> = mutableListOf()
    private val predecessors: MutableList<Step> = mutableListOf()
    private var available = true
    private var processed = false
    val isAvailable get() = available

    fun addSuccessor(successor: Step) {
        successor.available = false
        successors += successor
    }

    fun process() {
        processed = true
        available = false
        unlockSuccessorsIfPossible()
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