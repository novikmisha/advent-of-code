package _2023

import Day
import InputReader
import times

class Day02 : Day(2023, 2) {

    private val firstCubesSet = mapOf(
        "red" to 12,
        "green" to 13,
        "blue" to 14
    )

    override val firstTestAnswer = 8
    override val secondTestAnswer = 2286

    override fun first(input: InputReader) = input.asLines()
        .sumOf { line ->
            val gameId = line.split(" ")[1].dropLast(1).toInt()
            val allMatch = line.dropWhile { it != ':' }.drop(1).split(";").all { game ->
                game.split(",").forEach { countToBalls ->
                    val (count, color) = countToBalls.trim().split(" ")
                    if (firstCubesSet.getOrDefault(color, Int.MIN_VALUE) < count.toInt()) {
                        return@all false
                    }
                }
                true
            }
            return@sumOf if (allMatch) gameId else 0
        }

    override fun second(input: InputReader) = input.asLines()
        .sumOf { line ->
            val minCubeMap = mutableMapOf<String, Int>()
            line.dropWhile { it != ':' }.drop(1).split(";").forEach { game ->
                game.split(",").forEach { countToBalls ->
                    val (count, color) = countToBalls.trim().split(" ")
                    minCubeMap[color] = count.toInt().coerceAtLeast(minCubeMap.getOrDefault(color, Int.MIN_VALUE))
                }
            }
            minCubeMap.values.times()
        }
}

fun main() {
    Day02().solve()
}