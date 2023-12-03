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
        val map = input.asLines()

        val result = mutableListOf<Int>()

        map.forEachIndexed { x, line ->
            var start = 0
            line.forEachIndexed { y, char ->
                if (!char.isDigit()) {
                    line.substring(start, y).toIntOrNull()?.let { number ->
                        val coordinatesOfNumber = (start until y).map { position ->
                            Coordinate(x, position)
                        }

                        getCoordinatesAround(coordinatesOfNumber).forEach { coordinate ->
                            map.atCoordinates(coordinate)?.let {
                                if (!it.isDigit() && it != '.') {
                                    result.add(number)
                                    return@let
                                }
                            }
                        }
                    }
                    start = y + 1
                }
            }
            val y = line.length

            if (start < y) {
                line.substring(start, y).toIntOrNull()?.let { number ->
                    val coordinatesOfNumber = (start until y).map { position ->
                        Coordinate(x, position)
                    }

                    getCoordinatesAround(coordinatesOfNumber).forEach { coordinate ->
                        map.atCoordinates(coordinate)?.let {
                            if (!it.isDigit() && it != '.') {
                                result.add(number)
                                return@let
                            }
                        }
                    }
                }
            }
        }

        return result.sum()
    }

    override fun second(input: InputReader): Int {
        val map = input.asLines()

        val result = mutableMapOf<Coordinate, MutableList<Int>>()

        map.forEachIndexed { x, line ->
            var start = 0
            line.forEachIndexed { y, char ->
                if (!char.isDigit()) {
                    line.substring(start, y).toIntOrNull()?.let { number ->
                        val coordinatesOfNumber = (start until y).map { position ->
                            Coordinate(x, position)
                        }

                        getCoordinatesAround(coordinatesOfNumber).forEach { coordinate ->
                            map.atCoordinates(coordinate)?.let {
                                if (it == '*') {
                                    result.getOrPut(coordinate) {
                                        mutableListOf()
                                    }.add(number)
                                    return@let
                                }
                            }
                        }
                    }
                    start = y + 1
                }
            }
            val y = line.length

            if (start < y) {
                line.substring(start, y).toIntOrNull()?.let { number ->
                    val coordinatesOfNumber = (start until y).map { position ->
                        Coordinate(x, position)
                    }

                    getCoordinatesAround(coordinatesOfNumber).forEach { coordinate ->
                        map.atCoordinates(coordinate)?.let {
                            if (it == '*') {
                                result.getOrPut(coordinate) {
                                    mutableListOf()
                                }.add(number)
                                return@let
                            }
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