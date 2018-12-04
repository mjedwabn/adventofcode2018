package io.github.mjedwabn.adventofcode2018.day4

import java.io.File
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun main(args: Array<String>) {
    println(Day4().run())
}

internal class Day4 {
    fun run(): Int {
        return strategy1(InputParser().parse())
    }

    fun strategy1(records: List<Record>): Int {
        val guardRecords = records.groupBy { r -> r.guardID }
        val guardSleepTimes = guardRecords.map { it.key to calculateSleepTime(it.value) }
        val guardThatHasMostMinutesAsleep = guardSleepTimes.sortedByDescending { it.second }.first().first
        val minuteSpendMostAsleep = findMinuteSpendMostAsleep(guardRecords.getOrDefault(guardThatHasMostMinutesAsleep, emptyList()))
        return guardThatHasMostMinutesAsleep * minuteSpendMostAsleep
    }

    private fun calculateSleepTime(records: List<Record>): Duration =
            records.withIndex()
                    .groupBy { it.index / 2 }
                    .map { it.value.map { it.value } }
                    .map { Duration.between(it[0].timestamp, it[1].timestamp) }
                    .reduce { acc, duration -> acc + duration }

    private fun findMinuteSpendMostAsleep(records: List<Record>): Int =
            records.withIndex()
                    .groupBy { it.index / 2 }
                    .map { it.value.map { it.value } }
                    .flatMap { (it[0].timestamp.minute..(it[1].timestamp.minute - 1)) }
                    .groupBy { it -> it }
                    .map { it.key to it.value.size }
                    .sortedByDescending { it.second }
                    .first()
                    .first
}

internal data class Record(val timestamp: LocalDateTime, val guardID: Int, val event: GuardEvent)

class InputParser {
    private val shitBeginningRegex: Regex = """\[(\d+-\d+-\d+ \d+:\d+)] Guard #(\d+) begins shift""".toRegex()
    private val fallAsleepRegex: Regex = """\[(\d+-\d+-\d+ \d+:\d+)] falls asleep""".toRegex()
    private val awakeningRegex: Regex = """\[(\d+-\d+-\d+ \d+:\d+)] wakes up""".toRegex()
    private val timestampPattern: String = "yyyy-MM-dd HH:mm"
    private val dateTimeFormatter = DateTimeFormatter.ofPattern(timestampPattern)

    internal fun parse(): List<Record> {
        val inputPath = javaClass.classLoader.getResource("input").path
        return toRecords(File(inputPath).readLines()
                .map { parseRecord(it) }
                .sortedBy { it.timestamp })
    }

    private fun parseRecord(record: String): QuasiRecord {
        val shiftBeginningMatch = shitBeginningRegex.find(record)
        val fallAsleepMatch = fallAsleepRegex.find(record)
        val awakeningMatch = awakeningRegex.find(record)

        if (shiftBeginningMatch != null) {
            val (timestamp, guardID) = shiftBeginningMatch.destructured;
            return QuasiRecord(parseTimestamp(timestamp), guardID.toInt(), GuardEvent.ShiftStart)
        } else if (fallAsleepMatch != null) {
            val (timestamp) = fallAsleepMatch.destructured;
            return QuasiRecord(parseTimestamp(timestamp), null, GuardEvent.FallAsleep)
        } else {
            val (timestamp) = awakeningMatch!!.destructured;
            return QuasiRecord(parseTimestamp(timestamp), null, GuardEvent.Awakening)
        }
    }

    private fun parseTimestamp(timestamp: String) =
            LocalDateTime.parse(timestamp, dateTimeFormatter)

    private fun toRecords(records: List<QuasiRecord>): List<Record> {
        val ret: MutableList<Record> = mutableListOf()
        var guardID: Int = records[0].guardID!!
        for (record in records) {
            if (record.guardID == null) {
                ret += Record(record.timestamp, guardID, record.event)
            } else {
                guardID = record.guardID
            }
        }
        return ret
    }
}

internal data class QuasiRecord(val timestamp: LocalDateTime, val guardID: Int?, val event: GuardEvent) {
    override fun toString(): String {
        return "QuasiRecord(timestamp=$timestamp, guardID=$guardID, event=$event)"
    }
}

enum class GuardEvent {
    ShiftStart,
    FallAsleep,
    Awakening
}
