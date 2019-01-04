package io.github.mjedwabn.adventofcode2018.day13

import spock.lang.Specification


class Day13Test extends Specification {
    def "sample paths"() {
        expect:
        new Day13().getLocationOfFirstCollision() == new Location(7, 3)
    }

    def "sample input"() {
        expect:
        new Day13().getLocationOfFirstCollision() == new Location(94, 65)
    }

    def "sample paths2"() {
        expect:
        new Day13().getLocationOfLastCart() == new Location(6, 4)
    }

    def "sample input2"() {
        expect:
        new Day13().getLocationOfLastCart() == new Location(0, 98)
    }
}
