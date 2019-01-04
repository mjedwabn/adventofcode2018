package io.github.mjedwabn.adventofcode2018.day14

fun main(args: Array<String>) {
    println(Day14().getNumberOfRecipesToTheLeft("880751"))
}

class Day14() {
    private val recipes: MutableList<Int> = mutableListOf()
    private var e1 = 0
    private var e2 = 1

    init {
        recipes += 3
        recipes += 7
    }

    fun getScoresOfTenRecipes(input: Int): String {
        while (recipes.size < input + 10) {
            makeRecipes()
            moveElves()
//            println(recipes)
        }
        return recipes.subList(input, input + 10).joinToString("")
    }

    fun getNumberOfRecipesToTheLeft(scores: String): Int {
        val lengthToFind = scores.length
        var numberOfRecipes = -1
        while (numberOfRecipes < 0) {
            makeRecipes()
            val recipesSize = recipes.size
            moveElves()
            val joined = recipes.subList(if (recipesSize - (lengthToFind + 1) < 0) 0 else recipesSize - (lengthToFind + 1), recipesSize).joinToString("")
            if (joined.contains(scores)) {
                numberOfRecipes = recipes.joinToString("").indexOf(scores)
            }
        }
        return numberOfRecipes
    }

    private fun moveElves() {
        e1 = moveElf(e1)
        e2 = moveElf(e2)
    }

    private fun moveElf(elf: Int): Int {
        val moves = recipes[elf] + 1
        return (elf + moves) % recipes.size
    }

    private fun makeRecipes() {
        val recipe1 = getCurrentRecipe(e1)
        val recipe2 = getCurrentRecipe(e2)

        val sum = recipe1 + recipe2
        val newRecipes = getDigits(sum)
        recipes.addAll(newRecipes)
    }

    private fun getDigits(sum: Int): List<Int> {
        return sum.toString().toCharArray().map { it.toInt() }.map { it - 48 }
    }

    private fun getCurrentRecipe(elf: Int): Int {
        return recipes[elf]
    }
}
