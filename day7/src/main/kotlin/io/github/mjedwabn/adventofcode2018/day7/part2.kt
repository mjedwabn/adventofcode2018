package io.github.mjedwabn.adventofcode2018.day7


fun main(args: Array<String>) {
    println(Day7Part2(5, 60).run())
}

internal class Day7Part2(private val workers: Int, private val baseStepTime: Int) {
    fun run(): Int {
        return processAndMeasureTime()
    }

    private fun processAndMeasureTime(): Int {
        val report = Graph(baseStepTime, workers).processAndMeasure(InputParser().parse())
        println(report)
        return report.second
    }
}
