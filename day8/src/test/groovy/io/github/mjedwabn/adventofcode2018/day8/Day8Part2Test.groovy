package io.github.mjedwabn.adventofcode2018.day8

import spock.lang.Specification

class Day8Part2Test extends Specification {
    def "sample tree"() {
        expect:
        new Day8Part2().run() == 66
    }
}
