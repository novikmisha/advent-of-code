package _2023

import Day
import InputReader
import indexOfFirstOrNull

class Day01 : Day(2023, 1) {

    private val numbers = listOf(
        "one", "two", "three", "four", "five", "six", "seven", "eight", "nine"
    )

    override val firstTestAnswer = 142
    override val secondTestAnswer = 281

    override fun first(input: InputReader) = input.asLines().sumOf { line ->
            line.firstNotNullOf { char -> char.digitToIntOrNull() } * 10 +
                    line.reversed().firstNotNullOf { char -> char.digitToIntOrNull() }
        }

    override fun second(input: InputReader) = input.asLines().sumOf {
            val sequence = it.windowedSequence(
                size = numbers.maxOf { number -> number.length }, partialWindows = true
            ).mapNotNull { window ->
                window.first().digitToIntOrNull()?.let { int -> return@mapNotNull int }

                numbers.indexOfFirstOrNull { entry ->
                    window.startsWith(entry)
                }?.inc()
            }
            sequence.first() * 10 + sequence.last()
        }
}

fun main() {
    Day01().solve(skipTest = true)
}