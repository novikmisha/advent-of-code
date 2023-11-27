package _2022

import readInputAsLine

fun main() {

    fun findPlaceOfNDistinctChars(input: String, n: Int): Int {
        for (i in n until input.length - 1) {
            val substring = input.substring(i - n, i)
            if (substring.toSet().size == n) {
                return i
            }
        }

        return -1
    }

    fun part1(input: String): Int {
        return findPlaceOfNDistinctChars(input, 4)
    }

    fun part2(input: String): Int {
        return findPlaceOfNDistinctChars(input, 14)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInputAsLine("Day06_test")
    println(part1(testInput))
    println(part2(testInput))

    val input = readInputAsLine("Day06")
    println(part1(input))
    println(part2(input))
}
