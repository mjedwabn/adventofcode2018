package io.github.mjedwabn.adventofcode2018.day4

fun main(args: Array<String>) {
    println(Day4Part2().run())
}

internal class Day4Part2 {
    internal fun run(): Int {
        return strategy2(InputParser().parse())
    }

    private fun strategy2(records: List<Record>): Int {
        val guardRecords = records.groupBy { r -> r.guardID }
        val guardAsleepFrequenciesPerMinute = findMinuteSpendAsleepFrequencies(guardRecords)
        val guardThatIsMostFrequentlyAsleepOnSameMinute = findMostFrequentlyAsleepGardOnSameMinute(guardAsleepFrequenciesPerMinute)
        return guardThatIsMostFrequentlyAsleepOnSameMinute.first * guardThatIsMostFrequentlyAsleepOnSameMinute.second
    }

    private fun findMostFrequentlyAsleepGardOnSameMinute(frequenciesPerMinute: Map<Int, Map<Int, Int>>): Pair<Int, Int> {
        val minutes = frequenciesPerMinute.flatMap { it.value.keys }.distinct()
        val sleepsPerMinute = minutes.map { s -> s to frequenciesPerMinute.map { it.key to it.value[s] }.toMap() }.toMap()
        val minute = sleepsPerMinute.map { it.key to maxAsleep(it.value) }.sortedByDescending { it.second }.first().first
        val guardID = sleepsPerMinute[minute]!!.map { it.key to it.value }.sortedByDescending { it.second }.first().first
        return Pair(guardID, minute)
    }

    private fun maxAsleep(frequencies: Map<Int, Int?>): Int {
        return frequencies.values.sortedByDescending { it }.filterNotNull().first()
    }

    private fun findMinuteSpendAsleepFrequencies(guardRecords: Map<Int, List<Record>>): Map<Int, Map<Int, Int>> {
        return guardRecords.map { it.key to frequencies(it.value) }.toMap()
    }

    private fun frequencies(records: List<Record>): Map<Int, Int> {
        return records.withIndex().groupBy { it.index / 2 }
                .map { it.value.map { it.value } }
                .flatMap { (it[0].timestamp.minute..(it[1].timestamp.minute - 1)) }
                .groupBy { it -> it }
                .map { it.key to it.value.size }
                .toMap()
    }
}
