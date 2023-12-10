package _2023

import Coordinate
import Day
import InputReader
import atCoordinates
import java.util.*

val directions = listOf(
    Coordinate(0, 1), //right
    Coordinate(1, 0), // bottom
    Coordinate(0, -1), // left
    Coordinate(-1, 0), // top
)

val RIGHT = directions[0]
val BOTTOM = directions[1]
val LEFT = directions[2]
val TOP = directions[3]

class Day10 : Day(2023, 10) {


    override val firstTestAnswer = 8
    override val secondTestAnswer = 10

    private val canGetFrom = mapOf(
        '|' to mapOf(
            TOP to setOf('|', 'F', '7'),
            BOTTOM to setOf('|', 'L', 'J')
        ),
        '-' to mapOf(
            LEFT to setOf('F', 'L', '-'),
            RIGHT to setOf('J', '7', '-')
        ),
        'L' to mapOf(
            TOP to setOf('|', 'F', '7'),
            RIGHT to setOf('-', 'J', '7')
        ),
        'J' to mapOf(
            TOP to setOf('|', '7', 'F'),
            LEFT to setOf('-', 'L', 'F')
        ),
        '7' to mapOf(
            LEFT to setOf('-', 'F', 'L'),
            BOTTOM to setOf('|', 'L', 'J')
        ),
        'F' to mapOf(
            RIGHT to setOf('-', 'J', '7'),
            BOTTOM to setOf('|', 'J', 'L')
        ),
    )

    private val connectionMap = mapOf(
        BOTTOM to setOf('|', 'J', 'L'),
        TOP to setOf('|', 'F', '7'),
        RIGHT to setOf('-', '7', 'J'),
        LEFT to setOf('-', 'F', 'L')
    )

    private fun getNearestPositions(
        position: Coordinate,
        visitedCoordinates: MutableSet<Coordinate>,
        schema: List<String>
    ): Coordinate {
        val char = schema.atCoordinates(position)!!

        for (direction in directions) {
            val nearPosition = position + direction

            if (visitedCoordinates.contains(nearPosition)) {
                continue
            }

            val connectedChar = schema.atCoordinates(nearPosition) ?: continue
            if (canGetFrom[char]?.get(direction)?.contains(connectedChar) == true) {
                return nearPosition
            }
        }
        error("no connected positions found for $position")
    }

    override fun first(input: InputReader): Int {
        val schema = input.asLines().toMutableList();
        lateinit var startPosition: Coordinate

        for (row in schema.indices) {
            for (column in schema[row].indices) {
                if (schema[row][column] == 'S') {
                    startPosition = Coordinate(row, column)
                    break;
                }
            }
        }


        val (firstDirection, secondDirection) = directions.filter { direction ->
            val char = schema.atCoordinates(startPosition + direction)
            char !in connectionMap[direction]!!
        }

        val startChar = connectionMap[firstDirection]!!.intersect(connectionMap[secondDirection]!!).first()
        schema[startPosition.x] = schema[startPosition.x].replaceFirst("S", startChar.toString())

        val tubePositions =
            directions.filter { direction -> direction != firstDirection && direction != secondDirection }

        var position = startPosition + tubePositions.first()
        val finish = startPosition + tubePositions.last()

        val loop = mutableSetOf<Coordinate>().also {
            it.add(position)
            it.add(startPosition)
        }

        while (position != finish) {
            position = getNearestPositions(position, loop, schema)
            loop.add(position)
        }

        return loop.size / 2
    }

    override fun second(input: InputReader): Int {
        val schema = input.asLines().toMutableList();
        lateinit var startPosition: Coordinate

        for (row in schema.indices) {
            for (column in schema[row].indices) {
                if (schema[row][column] == 'S') {
                    startPosition = Coordinate(row, column)
                    break;
                }
            }
        }


        val (firstDirection, secondDirection) = directions.filter { direction ->
            val char = schema.atCoordinates(startPosition + direction)
            char !in connectionMap[direction]!!
        }

        val startChar = connectionMap[firstDirection]!!.intersect(connectionMap[secondDirection]!!).first()
        schema[startPosition.x] = schema[startPosition.x].replaceFirst("S", startChar.toString())

        val tubePositions =
            directions.filter { direction -> direction != firstDirection && direction != secondDirection }

        var position = startPosition + tubePositions.first()
        val finish = startPosition + tubePositions.last()

        val loop = mutableSetOf<Coordinate>().also {
            it.add(position)
            it.add(startPosition)
        }

        while (position != finish) {
            position = getNearestPositions(position, loop, schema)
            loop.add(position)
        }

        loop.add(finish)
        loop.add(startPosition)

        // https://en.wikipedia.org/wiki/Even%E2%80%93odd_rule
        val inside = mutableSetOf<Coordinate>()
        for (row in schema.indices) {
            for (column in schema[row].indices) {
                val coordinate = Coordinate(row, column)

                if (coordinate in loop) {
                    continue
                }

                var directionCounter = 0

                val direction = BOTTOM
                var nextCoordinate = coordinate + direction
                while (schema.atCoordinates(nextCoordinate) != null) {
                    if (nextCoordinate in loop && connectionMap[LEFT]?.contains(schema.atCoordinates(nextCoordinate)) == true) {
                        directionCounter++
                    }
                    nextCoordinate += direction
                }
                if (directionCounter % 2 == 1) {
                    inside.add(Coordinate(row, column))
                }
            }
        }

        return inside.size
    }
}

fun main() {
    Day10().solve(skipFirst = true)
}