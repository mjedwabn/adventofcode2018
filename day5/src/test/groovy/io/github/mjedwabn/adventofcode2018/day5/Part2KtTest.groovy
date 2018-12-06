package io.github.mjedwabn.adventofcode2018.day5

import spock.lang.Specification


class Part2KtTest extends Specification {
    def "sample polymer"() {
        expect:
        new Day5Part2().getRemainingUnits("dabAcCaCBAcCcaDA") == 4
    }
}
