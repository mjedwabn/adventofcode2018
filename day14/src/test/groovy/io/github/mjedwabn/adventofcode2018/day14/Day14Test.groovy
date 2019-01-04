package io.github.mjedwabn.adventofcode2018.day14

import spock.lang.Specification


class Day14Test extends Specification {
    def "sample recipes"() {
        expect:
        new Day14().getScoresOfTenRecipes(recipes) == score

        where:
        recipes || score
        9       || "5158916779"
        5       || "0124515891"
        18      || "9251071085"
        2018    || "5941429882"
    }

    def "part1 input"() {
        expect:
        new Day14().getScoresOfTenRecipes(recipes) == score

        where:
        recipes || score
        880751  || "3656126723"
    }

    def "part 2 samples"() {
        expect:
        new Day14().getNumberOfRecipesToTheLeft(scores) == recipes

        where:
        scores  || recipes
        "51589" || 9
        "01245" || 5
        "92510" || 18
        "59414" || 2018
        "245"   || 7
        "916"   || 13
    }

    def "part 2 input"() {
        expect:
        new Day14().getNumberOfRecipesToTheLeft(scores) == recipes

        where:
        scores   || recipes
        "880751" || 20333868
    }
}
