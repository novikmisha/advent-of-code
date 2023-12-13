package _2023

import Day
import InputReader

class Day13 : Day(2023, 13) {

    override val firstTestAnswer = 405
    override val secondTestAnswer = 400

    private fun checkHorizontal(pattern: List<String>, smudge: Int): Int {
        loop@ for (i in 0 until pattern.size - 1) {
            var leftSmudge = smudge
            var first = i
            var second = i + 1
            do {
                for (char in 0 until pattern.first().length) {
                    if (pattern[first][char] != pattern[second][char]) {
                        leftSmudge--
                        if (leftSmudge < 0) {
                            continue@loop
                        }
                    }
                }
                first--
                second++
            } while (first >= 0 && second < pattern.size)

            if (leftSmudge != 0) {
                continue
            }
            return (i + 1) * 100
        }
        return 0
    }

    private fun checkVertical(
        pattern: List<String>,
        smudge: Int
    ): Int {
        loop@ for (i in 0 until pattern.first().length - 1) {
            var first = i
            var second = i + 1
            var leftSmudge = smudge
            do {
                for (row in pattern) {
                    if (row[first] != row[second]) {
                        leftSmudge--
                        if (leftSmudge < 0) {
                            continue@loop
                        }
                    }
                }

                first--
                second++
            } while (first >= 0 && second < pattern.first().length - 1)
            if (leftSmudge != 0) {
                continue
            }
            return i + 1
        }
        return 0
    }

    override fun first(input: InputReader) = input.asGroups().sumOf { pattern ->
        var result = checkHorizontal(pattern, 0)
        if (result == 0) {
            result = checkVertical(pattern, 0)
        }

        result
    }

    override fun second(input: InputReader) = input.asGroups().sumOf { pattern ->
        var result = checkHorizontal(pattern, 1)
        if (result == 0) {
            result = checkVertical(pattern, 1)
        }

        result
    }
}

fun main() {
    Day13().solve()
}