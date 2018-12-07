package io.github.mjedwabn.adventofcode2018.day7

import spock.lang.Specification


class Day7Test extends Specification {
    def "sample instructions"() {
        expect:
        order([['C', 'A'], ['C', 'F'], ['A', 'B'], ['A', 'D'], ['B', 'E'], ['D', 'E'], ['F', 'E']]) == "CABDFE"
    }

    def "sample input"() {
        expect:
        new Day7().run() == "CABDFE"
    }

    String order(List<List<String>> instructions) {
        return new Day7().getStepsOrder(instructions.stream()
                .map{i -> new Instruction(i[0], i[1])}
                .collect())
    }
}
