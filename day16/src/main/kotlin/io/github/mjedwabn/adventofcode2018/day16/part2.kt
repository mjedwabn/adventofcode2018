package io.github.mjedwabn.adventofcode2018.day16

import kotlin.reflect.KClass


fun main(args: Array<String>) {
    println(Day16Part2().run())
}

class Day16Part2 {
    fun run(): Int {
        val (samples, testProgram) = InputParser().parse()
        val opcodeLearner = OpcodeLearner()
        opcodeLearner.learn(samples)
        val interpreter = Interpreter(opcodeLearner.getOpcodesMap())
        interpreter.execute(testProgram)
        return interpreter.getState()[0]
    }
}

class Interpreter(private val opcodesMap: Map<Int, KClass<out Opcode>>) {
    private var cpu: CPU = CPU()

    fun execute(testProgram: List<Instruction>) {
        cpu = CPU()
        testProgram.forEach { execute(it) }
    }

    private fun execute(instruction: Instruction) {
        val opcode = getAvailableOpcodes()
                .map { it(cpu) }
                .first { it::class == opcodesMap[instruction.opcode] }
        opcode.execute(instruction.a, instruction.b, instruction.out)
    }

    fun getState(): List<Int> {
        return cpu.readRegisters()
    }
}

class OpcodeLearner {
    private val learnedOpcodes: MutableMap<Int, KClass<out Opcode>> = mutableMapOf()

    fun learn(samples: List<Sample>) {
        while (unresolvedOpcodesExist(samples)) {
            resolveOpcodes(samples)
        }
    }

    fun getOpcodesMap(): MutableMap<Int, KClass<out Opcode>> {
        return learnedOpcodes
    }

    private fun resolveOpcodes(samples: List<Sample>) {
        samples.forEach {
            val matchingOpcodes = OpcodeAnalyzer().getMatchingOpcodes(it)
            val unknownOpcodes = matchingOpcodes.minus(learnedOpcodes.values)
            if (unknownOpcodes.size == 1) {
                learnedOpcodes[it.instruction[0]] = unknownOpcodes[0]
            }
        }
    }

    private fun unresolvedOpcodesExist(samples: List<Sample>): Boolean {
        return samples.any { !learnedOpcodes.containsKey(it.instruction[0]) }
    }
}
