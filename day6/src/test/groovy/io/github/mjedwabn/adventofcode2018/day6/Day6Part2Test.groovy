package io.github.mjedwabn.adventofcode2018.day6

import spock.lang.Specification


class Day6Part2Test extends Specification {
    def "sample input"() {
        expect:
        new Day6Part2().run(32) == 16
    }
}
