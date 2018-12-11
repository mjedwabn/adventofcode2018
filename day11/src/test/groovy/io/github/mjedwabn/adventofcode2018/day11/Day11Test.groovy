package io.github.mjedwabn.adventofcode2018.day11

import kotlin.Pair
import spock.lang.Specification


class Day11Test extends Specification {
    def "sample power levels"() {
        given:
        def serialNumber = 8

        when:
        def powerLevel = new WristMountedDevice(serialNumber).getPowerLevelOfCell(3, 5)

        then:
        powerLevel == 4
    }

    def "more sample power levels"() {
        expect:
        new WristMountedDevice(serialNumber).getPowerLevelOfCell(x, y) == powerLevel

        where:
        x   | y   | serialNumber || powerLevel
        122 | 79  | 57           || -5
        217 | 196 | 39           || 0
        101 | 153 | 71           || 4
    }

    def "sample largest total power cell"() {
        expect:
        new WristMountedDevice(serialNumber).getLargestTotalPowerCell() == new Pair(x, y)
        where:
        serialNumber || x  | y
        18           || 33 | 45
        42           || 21 | 61
    }
}


class PartTwo extends Specification {
    def "sample largest total square"() {
        expect:
        new WristMountedDevice(serialNumber).getLargestTotalSquareIdentifier() == new SquareIdentifier(x, y, size)

        where:
        serialNumber || x  | y   | size
        18           || 90 | 269 | 16
    }
}