package io.github.mjedwabn.adventofcode2018.day9

import spock.lang.Specification

class MarblesGameTest extends Specification {
    def "sample game"() {
        expect:
        new Day9().getHighScore(9, 25) == 32
    }

    def "sample input"() {
        expect:
        new Day9().run() == 8317
    }

    def "other samples"() {
        expect:
        new Day9().getHighScore(players, lastMarble) == score

        where:
        players | lastMarble || score
        10      | 1618       || 8317
        13      | 7999       || 146373
        17      | 1104       || 2764
        21      | 6111       || 54718
        30      | 5807       || 37305
    }

    def "official game"() {

    }
}
