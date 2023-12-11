package _2023

import Coordinate
import Day
import InputReader
import atCoordinates
import kotlin.math.abs

class Day11 : Day(2023, 11) {


    override val firstTestAnswer = 374
    override val secondTestAnswer = 0

    override fun first(input: InputReader): Int {
        val space = input.asLines().toMutableList()

        val galaxies = space.flatMapIndexed { row: Int, line: String ->
            line.mapIndexedNotNull { column, c ->
                val coordinate = Coordinate(row, column)
                if (space.atCoordinates(coordinate) == '#') {
                    coordinate
                } else {
                    null
                }
            }
        }

        val emptyRows = space.mapIndexedNotNull { row, line ->
            if (line.all { it == '.' }) {
                row
            } else {
                null
            }
        }

        val emptyColumns = mutableListOf<Int>()
        for (column in space.first().indices) {
            if (space.all { it[column] == '.' }) {
                emptyColumns.add(column)
            }
        }

        return galaxies.windowed(galaxies.size, partialWindows = true) {
            val startingGalaxy = it.first()
            val otherGalaxies = it.drop(1)
            otherGalaxies.map { galaxy ->
                var distance = abs(startingGalaxy.x - galaxy.x) + abs(startingGalaxy.y - galaxy.y)

                val xRange = listOf(startingGalaxy.x, galaxy.x).sorted()
                distance += emptyRows.count { it in xRange.first()..xRange.last() }

                val yRange = listOf(startingGalaxy.y, galaxy.y).sorted()
                distance += emptyColumns.count { it in yRange.first()..yRange.last() }
                distance
            }
        }.flatten().sum()
    }

    override fun second(input: InputReader): Long {
        val space = input.asLines().toMutableList()

        val galaxies = space.flatMapIndexed { row: Int, line: String ->
            line.mapIndexedNotNull { column, c ->
                val coordinate = Coordinate(row, column)
                if (space.atCoordinates(coordinate) == '#') {
                    coordinate
                } else {
                    null
                }
            }
        }

        val emptyRows = space.mapIndexedNotNull { row, line ->
            if (line.all { it == '.' }) {
                row
            } else {
                null
            }
        }

        val emptyColumns = mutableListOf<Int>()
        for (column in space.first().indices) {
            if (space.all { it[column] == '.' }) {
                emptyColumns.add(column)
            }
        }

        return galaxies.windowed(galaxies.size, partialWindows = true) {
            val startingGalaxy = it.first()
            val otherGalaxies = it.drop(1)
            otherGalaxies.map { galaxy ->
                var distance = (abs(startingGalaxy.x - galaxy.x) + abs(startingGalaxy.y - galaxy.y)).toLong()

                val xRange = listOf(startingGalaxy.x, galaxy.x).sorted()
                distance += (emptyRows.count { it in xRange.first()..xRange.last() } * 999999L)

                val yRange = listOf(startingGalaxy.y, galaxy.y).sorted()
                distance += (emptyColumns.count { it in yRange.first()..yRange.last() } * 999999L)
                distance
            }
        }.flatten().sum()
    }
}

fun main() {
    Day11().solve(skipTest = true)
}