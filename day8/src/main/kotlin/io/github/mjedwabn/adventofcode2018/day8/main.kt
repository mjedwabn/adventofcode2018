package io.github.mjedwabn.adventofcode2018.day8

import java.io.File


fun main(args: Array<String>) {
    println(Day8().run())
}

internal class Day8 {
    fun run(): Int {
        val inputPath = javaClass.classLoader.getResource("input").path
        val treeInput = File(inputPath).readLines()
                .first().split(' ')
                .map { it.toInt() }
        return getMetaDataEntriesSum(treeInput)
    }

    fun getMetaDataEntriesSum(treeInput: List<Int>): Int {
        val root: Node = TreeBuilder(treeInput).build()
        return getMetaDataEntriesSum(root)
    }

    private fun getMetaDataEntriesSum(node: Node): Int {
        return node.metadataEntries.sum() + node.children.map { getMetaDataEntriesSum(it) }.sum()
    }
}

internal class TreeBuilder(private val treeInput: List<Int>) {
    fun build(): Node {
        return buildNode(treeInput.listIterator())
    }

    private fun buildNode(inputPointer: ListIterator<Int>): Node {
        val header = Header(inputPointer.next(), inputPointer.next())
        val children = (0 until header.quantityOfChildNodes).map { buildNode(inputPointer) }
        val metadata = (0 until header.quantityOfMetadataEntries).map { inputPointer.next() }
        return Node(header, children, metadata)
    }
}

internal data class Node(val header: Header, val children: List<Node>, val metadataEntries: List<Int>)

internal data class Header(val quantityOfChildNodes: Int, val quantityOfMetadataEntries: Int)