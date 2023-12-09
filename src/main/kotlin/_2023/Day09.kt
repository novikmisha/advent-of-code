package _2023

import Day
import InputReader

class Day09 : Day(2023, 9) {

    override val firstTestAnswer = 114L
    override val secondTestAnswer = 2L


    override fun first(input: InputReader) =
        input.asLines().sumOf { line ->
            val rows = mutableListOf(numRegex.findAll(line).map { it.value.toLong() }.toList())
            while (!rows.last().all { it == 0L }) {
                rows.add(rows.last().zipWithNext().map { it.second - it.first })
            }

            rows.map { it.last() }.reversed().reduce { result, currentElement -> currentElement + result }
        }

    override fun second(input: InputReader) =
        input.asLines().sumOf { line ->
            val rows = mutableListOf(numRegex.findAll(line).map { it.value.toLong() }.toList())
            while (!rows.last().all { it == 0L }) {
                rows.add(rows.last().zipWithNext().map { it.second - it.first })
            }

            rows.map { it.first() }.reversed().reduce { result, currentElement -> currentElement - result }
        }
}

fun main() {
    Day09().solve()
}