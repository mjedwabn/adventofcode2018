package io.github.mjedwabn.adventofcode2018.day6

import spock.lang.Specification


class Day6Test extends Specification {
    def "sample coordinates"() {
        expect:
        new Day6().findSizeOfLargestFiniteArea([
                new Coordinate(1, 1, 1),
                new Coordinate(1, 6, 2),
                new Coordinate(8, 3, 3),
                new Coordinate(3, 4, 4),
                new Coordinate(5, 5, 5),
                new Coordinate(8, 9, 6)]) == 17
    }

    def "sample input"() {
        expect:
        new Day6().run() == 17
    }
}
