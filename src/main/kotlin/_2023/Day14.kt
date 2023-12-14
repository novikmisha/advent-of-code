package _2023

import Coordinate
import Day
import InputReader
import atCoordinates

class Day14 : Day(2023, 14) {

    override val firstTestAnswer = 136
    override val secondTestAnswer = 64


    override fun first(input: InputReader): Int {
        val schema = input.asLines().map { it.toCharArray() }
        for (row in schema.indices) {
            for (column in 0 until schema[row].size) {
                if (schema[row][column] == 'O') {
                    var newRow = row - 1
                    while (newRow >= 0 && schema[newRow][column] == '.') {
                        newRow--
                    }
                    schema[row][column] = '.'
                    schema[newRow + 1][column] = 'O'
                }
            }
        }

        return schema.mapIndexed { index, row -> row.count { it == 'O' } * (schema.size - index) }.sum()
    }

    override fun second(input: InputReader): Int {

        data class Rock(
            var coordinate: Coordinate
        )

        val directions = listOf(
            Coordinate(-1, 0), // top
            Coordinate(0, -1), // left
            Coordinate(1, 0), // bottom
            Coordinate(0, 1), //right
        )

        val schema = input.asLines().map { it.toCharArray() }
        val rocks = mutableSetOf<Rock>()

        for (row in schema.indices) {
            for (column in 0 until schema[row].size) {
                if (schema[row][column] == 'O') {
                    rocks.add(Rock(Coordinate(row, column)))
                }
            }
        }

        val cycleHistories = mutableListOf<List<Rock>>()
        var cycle = 0
        do {
            directions.forEach { direction ->
                rocks.sortedBy {
                    if (direction.x != 0) {
                        it.coordinate.x * -direction.x
                    } else {
                        it.coordinate.y * -direction.y
                    }
                }.forEach { rock ->
                    var newCoordinate = rock.coordinate + direction
                    while (schema.atCoordinates(newCoordinate) == '.') {
                        newCoordinate += direction
                    }

                    newCoordinate -= direction

                    schema[rock.coordinate.x][rock.coordinate.y] = '.'
                    schema[newCoordinate.x][newCoordinate.y] = 'O'

                    rock.coordinate = newCoordinate
                }
            }


            val currentRocks = rocks.map { it.copy() }
            val cycleStart = cycleHistories.indexOfFirst { it.containsAll(currentRocks) }

            if (cycleStart >= 0) {
                val endIndex = (1000000000 - cycleStart + 1) % (cycle - cycleStart) - 1
                val endRocks = cycleHistories[cycleStart + endIndex - 1]

                return endRocks.groupBy { it.coordinate.x }.map { (schema.size - it.key) * it.value.size }.sum()
            } else {
                cycleHistories.add(currentRocks)
                cycle++
            }
        } while (cycle < 1000000000)
        error("cycle not found ")
    }
}

fun main() {
    Day14().solve()
}