package _2023

import Coordinate
import Day
import InputReader
import atCoordinates
import getCoordinatesAround
import times

class Day03 : Day(2023, 3) {

    override val firstTestAnswer = 4361
    override val secondTestAnswer = 467835


    override fun first(input: InputReader): Int {
        val schematic = input.asLines()

        return schematic.flatMapIndexed { x, line ->

            numRegex.findAll(line).mapNotNull { matchResult ->
                val number = matchResult.value.toInt()

                val coordinatesOfNumber = (matchResult.range.first..matchResult.range.last).map { position ->
                    Coordinate(x, position)
                }

                getCoordinatesAround(coordinatesOfNumber).forEach { coordinate ->
                    schematic.atCoordinates(coordinate)?.let {
                        if (!it.isDigit() && it != '.') {
                            return@mapNotNull number
                        }
                    }
                }

                return@mapNotNull null
            }

        }.sum()
    }

    override fun second(input: InputReader): Int {
        val schematic = input.asLines()

        val result = mutableMapOf<Coordinate, MutableList<Int>>()

        schematic.forEachIndexed { x, line ->
            numRegex.findAll(line).forEach { matchResult ->

                val number = matchResult.value.toInt()

                val coordinatesOfNumber = (matchResult.range.first..matchResult.range.last).map { position ->
                    Coordinate(x, position)
                }

                getCoordinatesAround(coordinatesOfNumber).forEach { coordinate ->
                    schematic.atCoordinates(coordinate)?.let {
                        if (it == '*') {
                            result.getOrPut(coordinate) { mutableListOf() }.add(number)
                        }
                    }
                }
            }
        }

        return result.values.sumOf { if (it.size == 2) it.times() else 0 }
    }
}


fun main() {
    Day03().solve()
}