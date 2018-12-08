package io.github.mjedwabn.adventofcode2018.day7

import spock.lang.Specification


class Day7Part2Test extends Specification {
    def "sample input"() {
        expect:
        new Day7Part2(2, 0).run() == 15
    }
}
