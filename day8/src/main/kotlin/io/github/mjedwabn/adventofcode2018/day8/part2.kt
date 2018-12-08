package io.github.mjedwabn.adventofcode2018.day8

import java.io.File

fun main(args: Array<String>) {
    println(Day8Part2().run())
}

class Day8Part2 {
    fun run(): Int = getValueOfRootNode(parseInput())

    private fun parseInput(): List<Int> {
        val inputPath = javaClass.classLoader.getResource("input").path
        return File(inputPath).readLines()
                .first().split(' ')
                .map { it.toInt() }
    }

    private fun getValueOfRootNode(treeInput: List<Int>): Int =
            getValueOfNode(TreeBuilder(treeInput).build())

    private fun getValueOfNode(node: Node): Int = when {
        node.children.isEmpty() -> node.metadataEntries.sum()
        else -> node.metadataEntries
                .filter { it > 0 }
                .filter { it <= node.header.quantityOfChildNodes }
                .map { getValueOfNode(node.children[it - 1]) }
                .sum()
    }
}
