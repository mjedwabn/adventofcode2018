package io.github.mjedwabn.adventofcode2018.day16

import java.io.File
import kotlin.reflect.KClass


fun main(args: Array<String>) {
    println(Day16().run())
}

class Day16 {
    fun run(): Int {
        val samples = InputParser().parse()
        return OpcodeAnalyzer().howManySamplesBehaveLikeThreeOrMoreOpcodes(samples.first)
    }
}

class OpcodeAnalyzer {
    fun howManySamplesBehaveLikeThreeOrMoreOpcodes(samples: List<Sample>): Int {
        return samples.map { howManyOpcodesMatchSample(it) }
                .filter { it >= 3 }
                .size
    }

    private fun howManyOpcodesMatchSample(sample: Sample): Int {
        return getMatchingOpcodes(sample).size
    }

    fun getMatchingOpcodes(sample: Sample): List<KClass<out Opcode>> {
        return getAvailableOpcodes().filter { matches(it, sample) }.map { it(CPU())::class }
    }

    private fun matches(opcodeProvider: (CPU) -> Opcode, sample: Sample): Boolean {
        val cpu = CPU()
        cpu.setRegisters(sample.before)
        val instruction = Instruction(sample.instruction[0], sample.instruction[1], sample.instruction[2], sample.instruction[3])
        opcodeProvider(cpu).execute(instruction.a, instruction.b, instruction.out)
        return cpu.readRegisters() == sample.after
    }
}

fun getAvailableOpcodes(): List<(CPU) -> Opcode> {
    return listOf(
            { cpu: CPU -> Addr(cpu) },
            { cpu: CPU -> Addi(cpu) },
            { cpu: CPU -> Mulr(cpu) },
            { cpu: CPU -> Muli(cpu) },
            { cpu: CPU -> Banr(cpu) },
            { cpu: CPU -> Bani(cpu) },
            { cpu: CPU -> Borr(cpu) },
            { cpu: CPU -> Bori(cpu) },
            { cpu: CPU -> Setr(cpu) },
            { cpu: CPU -> Seti(cpu) },
            { cpu: CPU -> Gtir(cpu) },
            { cpu: CPU -> Gtri(cpu) },
            { cpu: CPU -> Gtrr(cpu) },
            { cpu: CPU -> Eqir(cpu) },
            { cpu: CPU -> Eqri(cpu) },
            { cpu: CPU -> Eqrr(cpu) }
    )
}

class CPU {
    private val numberOfRegisters: Int = 4
    private val registerInitialValue: Int = 0
    private val registersNumberedFrom: Int = 0

    private val registers: MutableMap<Int, Int>

    init {
        this.registers = (registersNumberedFrom until (registersNumberedFrom + numberOfRegisters))
                .map { it to registerInitialValue }
                .toMap().toMutableMap()
    }

    fun readRegister(register: Int): Int {
        return registers[register] ?: registerInitialValue
    }

    fun writeRegister(register: Int, value: Int) {
        registers[register] = value
    }

    fun setRegisters(values: List<Int>) {
        values.withIndex().forEach {
            writeRegister(it.index + registersNumberedFrom, it.value)
        }
    }

    fun readRegisters(): List<Int> {
        return (registersNumberedFrom until (registersNumberedFrom + numberOfRegisters))
                .map { readRegister(it) }
    }
}

data class Instruction(val opcode: Int, val a: Int, val b: Int, val out: Int)

abstract class Opcode(private val cpu: CPU) {
    abstract fun execute(a: Int, b: Int, out: Int)

    fun read(register: Int): Int {
        return cpu.readRegister(register)
    }

    fun write(register: Int, value: Int) {
        cpu.writeRegister(register, value)
    }
}

class Addr(cpu: CPU) : Opcode(cpu) {
    override fun execute(a: Int, b: Int, out: Int) {
        write(out, read(a) + read(b))
    }
}

class Addi(cpu: CPU) : Opcode(cpu) {
    override fun execute(a: Int, b: Int, out: Int) {
        write(out, read(a) + b)
    }
}

class Mulr(cpu: CPU) : Opcode(cpu) {
    override fun execute(a: Int, b: Int, out: Int) {
        write(out, read(a) * read(b))
    }
}

class Muli(cpu: CPU) : Opcode(cpu) {
    override fun execute(a: Int, b: Int, out: Int) {
        write(out, read(a) * b)
    }
}

class Banr(cpu: CPU) : Opcode(cpu) {
    override fun execute(a: Int, b: Int, out: Int) {
        write(out, read(a) and read(b))
    }
}

class Bani(cpu: CPU) : Opcode(cpu) {
    override fun execute(a: Int, b: Int, out: Int) {
        write(out, read(a) and b)
    }
}

class Borr(cpu: CPU) : Opcode(cpu) {
    override fun execute(a: Int, b: Int, out: Int) {
        write(out, read(a) or read(b))
    }
}

class Bori(cpu: CPU) : Opcode(cpu) {
    override fun execute(a: Int, b: Int, out: Int) {
        write(out, read(a) or b)
    }
}

class Setr(cpu: CPU) : Opcode(cpu) {
    override fun execute(a: Int, b: Int, out: Int) {
        write(out, read(a))
    }
}

class Seti(cpu: CPU) : Opcode(cpu) {
    override fun execute(a: Int, b: Int, out: Int) {
        write(out, a)
    }
}

class Gtir(cpu: CPU) : Opcode(cpu) {
    override fun execute(a: Int, b: Int, out: Int) {
        write(out, if (a > read(b)) 1 else 0)
    }
}

class Gtri(cpu: CPU) : Opcode(cpu) {
    override fun execute(a: Int, b: Int, out: Int) {
        write(out, if (read(a) > b) 1 else 0)
    }
}

class Gtrr(cpu: CPU) : Opcode(cpu) {
    override fun execute(a: Int, b: Int, out: Int) {
        write(out, if (read(a) > read(b)) 1 else 0)
    }
}

class Eqir(cpu: CPU) : Opcode(cpu) {
    override fun execute(a: Int, b: Int, out: Int) {
        write(out, if (a == read(b)) 1 else 0)
    }
}

class Eqri(cpu: CPU) : Opcode(cpu) {
    override fun execute(a: Int, b: Int, out: Int) {
        write(out, if (read(a) == b) 1 else 0)
    }
}

class Eqrr(cpu: CPU) : Opcode(cpu) {
    override fun execute(a: Int, b: Int, out: Int) {
        write(out, if (read(a) == read(b)) 1 else 0)
    }
}

class InputParser {
    private val beforeRegex: Regex = """Before:\s+\[(\d+), (\d+), (\d+), (\d+)]""".toRegex()
    private val afterRegex: Regex = """After:\s+\[(\d+), (\d+), (\d+), (\d+)]""".toRegex()
    private val instructionRegex: Regex = """(\d+) (\d+) (\d+) (\d+)""".toRegex()

    fun parse(): Pair<List<Sample>, List<Instruction>> {
        val inputPath = javaClass.classLoader.getResource("input").path
        val text = File(inputPath).readText()
        return parse(text)
    }

    fun parse(text: String): Pair<List<Sample>, List<Instruction>> {
        val split = text.split("\n\n\n\n", limit = 2)
        val samples = split[0].split("\n").chunked(4).map { makeSample(it) }
        val testProgram = if (split.size > 1) split[1].split("\n").map { makeInstruction(it) } else emptyList()
        return Pair(samples, testProgram)
    }

    private fun makeSample(sample: List<String>): Sample {
        val before = beforeRegex.find(sample[0])!!.destructured.toList().map { it.toInt() }
        val instruction = instructionRegex.find(sample[1])!!.destructured.toList().map { it.toInt() }
        val after = afterRegex.find(sample[2])!!.destructured.toList().map { it.toInt() }
        return Sample(before, instruction, after)
    }

    private fun makeInstruction(instruction: String): Instruction {
        val parts = instruction.split(" ").map { it.toInt() }
        return Instruction(parts[0], parts[1], parts[2], parts[3])
    }
}

data class Sample(val before: List<Int>, val instruction: List<Int>, val after: List<Int>) {
    override fun toString(): String {
        return "Sample(\nbefore=$before, \ninstruction=$instruction, \nafter=$after\n)"
    }
}
