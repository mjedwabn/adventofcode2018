package io.github.mjedwabn.adventofcode2018.day8

import spock.lang.Specification

class Day8Test extends Specification {
    def "sample tree"() {
        expect:
        metaSum([2, 3, 0, 3, 10, 11, 12, 1, 1, 0, 1, 99, 2, 1, 1, 2]) == 138
    }

    def "sample input"() {
        expect:
        new Day8().run() == 138
    }

    int metaSum(List<Integer> treeInput) {
        new Day8().getMetaDataEntriesSum(treeInput)
    }
}
