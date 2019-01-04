package io.github.mjedwabn.adventofcode2018.day16

import spock.lang.Specification


class Day16Test extends Specification {
    def "input parser"() {
        given:
        def input = '''Before: [3, 2, 1, 1]
                        9 2 1 2
                        After:  [3, 2, 2, 1]
                        '''

        expect:
        new InputParser().parse(input).getFirst() == [new Sample(
                [3, 2, 1, 1], [9, 2, 1, 2], [3, 2, 2, 1]
        )]
    }

    def "sample samples behaving like three or more opcodes"() {
        expect:
        new Day16().run() == 1
    }

    static class CPUTest extends Specification {
        def "initial state"() {
            given:
            def cpu = new CPU()

            expect:
            cpu.readRegister(register) == value

            where:
            register || value
            0        || 0
            1        || 0
            2        || 0
            3        || 0
        }

        def "set registers"() {
            given:
            def cpu = new CPU()

            when:
            cpu.setRegisters([3, 4, 5, 6])

            then:
            cpu.readRegister(register) == value

            where:
            register || value
            0        || 3
            1        || 4
            2        || 5
            3        || 6
        }

        def "write register"() {
            given:
            def cpu = new CPU()

            when:
            cpu.writeRegister(1, 3)

            then:
            cpu.readRegister(register) == value

            where:
            register || value
            0        || 0
            1        || 3
            2        || 0
            3        || 0
        }
    }
}
