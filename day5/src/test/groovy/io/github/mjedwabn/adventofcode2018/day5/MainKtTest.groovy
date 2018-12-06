package io.github.mjedwabn.adventofcode2018.day5

import spock.lang.Specification


class MainKtTest extends Specification {
    def "sample reactions"() {
        expect:
        new Day5().react(polymer) == reacted

        where:
        polymer            || reacted
        "aA"               || ""
        "abBA"             || ""
        "abAB"             || "abAB"
        "aabAAB"           || "aabAAB"
        "dabAcCaCBAcCcaDA" || "dabCBAcaDA"
    }

    def "sample polymer"() {
        expect:
        new Day5().getRemainingUnits("dabAcCaCBAcCcaDA") == 10
    }

    def "sample input"() {
        expect:
        new Day5().run() == 10
    }
}
