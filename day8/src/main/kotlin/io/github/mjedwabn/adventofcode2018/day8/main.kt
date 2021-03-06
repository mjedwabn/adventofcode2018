package io.github.mjedwabn.adventofcode2018.day8

import java.io.File

fun main(args: Array<String>) {
    println(Day8().run())
}

internal class Day8 {
    fun run(): Int = getMetaDataEntriesSum(parseInput())

    private fun parseInput(): List<Int> {
        val inputPath = javaClass.classLoader.getResource("input").path
        return File(inputPath).readLines()
                .first().split(' ')
                .map { it.toInt() }
    }

    fun getMetaDataEntriesSum(treeInput: List<Int>): Int =
            getMetaDataEntriesSum(TreeBuilder(treeInput).build())

    private fun getMetaDataEntriesSum(node: Node): Int =
            node.metadataEntries.sum() +
                    node.children.map { getMetaDataEntriesSum(it) }.sum()
}

internal class TreeBuilder(private val treeInput: List<Int>) {
    fun build(): Node = buildNode(treeInput.listIterator())

    private fun buildNode(inputPointer: ListIterator<Int>): Node {
        val header = Header(inputPointer.next(), inputPointer.next())
        val children = (0 until header.quantityOfChildNodes).map { buildNode(inputPointer) }
        val metadata = (0 until header.quantityOfMetadataEntries).map { inputPointer.next() }
        return Node(header, children, metadata)
    }
}

internal data class Node(val header: Header, val children: List<Node>, val metadataEntries: List<Int>)

internal data class Header(val quantityOfChildNodes: Int, val quantityOfMetadataEntries: Int)