package _2023

import Day
import InputReader
import times

class Day06 : Day(2023, 6) {

    override val firstTestAnswer = 288L
    override val secondTestAnswer = 71503uL

    private val numRegex = Regex("\\d+")

    override fun first(input: InputReader): Long {
        val (times, distances) = input.asLines().map {
            numRegex.findAll(it).mapTo(mutableListOf()) { number -> number.value.toLong() }
        }

        val races = times.zip(distances)

        return races.map {
            val (time, distance) = it
            var numberOfWinningHolds = 0L

            for (holdTime in 1 until time) {
                val traveledDistance = holdTime * (time - holdTime)
                if (traveledDistance > distance) {
                    numberOfWinningHolds++
                }
            }

            numberOfWinningHolds
        }.times()
    }

    override fun second(input: InputReader): ULong {
        val (time, distance) = input.asLines().map { line ->
            line.dropWhile { it != ':' }.drop(1).replace(" ", "").toULong()
        }

        var leastHoldTime = 0uL
        do {
            leastHoldTime++
        } while (leastHoldTime * (time - leastHoldTime) <= distance)

        println(leastHoldTime)
        return time - (leastHoldTime.times(2uL)) + 1uL
    }

}

fun main() {
    Day06().solve(skipFirst = true)
}