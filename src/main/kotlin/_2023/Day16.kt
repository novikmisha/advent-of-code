package _2023

import Coordinate
import Day
import InputReader
import atCoordinates

class Day16 : Day(2023, 16) {

    data class Beam(
        var coordinate: Coordinate,
        var direction: Coordinate
    )

    override val firstTestAnswer = 46
    override val secondTestAnswer = 51

    private fun getBeamsFromCoordinate(
        nextCoordinate: Coordinate,
        direction: Coordinate,
        schema: List<String>
    ): List<Beam> {
        val char = schema.atCoordinates(nextCoordinate) ?: return emptyList()
        return when (char) {
            '.' -> listOf(Beam(nextCoordinate, direction))
            '/' -> listOf(
                Beam(
                    nextCoordinate, when (direction) {
                        TOP -> RIGHT
                        RIGHT -> TOP
                        LEFT -> BOTTOM
                        BOTTOM -> LEFT
                        else -> error("")
                    }
                )
            )

            '\\' -> listOf(
                Beam(
                    nextCoordinate, when (direction) {
                        TOP -> LEFT
                        RIGHT -> BOTTOM
                        LEFT -> TOP
                        BOTTOM -> RIGHT
                        else -> error("")
                    }
                )
            )

            '|' -> {
                if (direction == TOP || direction == BOTTOM) {
                    listOf(Beam(nextCoordinate, direction))
                } else {
                    listOf(Beam(nextCoordinate, TOP), Beam(nextCoordinate, BOTTOM))
                }
            }

            '-' -> {
                if (direction == LEFT || direction == RIGHT) {
                    listOf(Beam(nextCoordinate, direction))
                } else {
                    listOf(Beam(nextCoordinate, LEFT), Beam(nextCoordinate, RIGHT))
                }
            }

            else -> error("unknown char $char")
        }
    }

    private fun nextPosition(beam: Beam, schema: List<String>): List<Beam> {
        val nextCoordinate = beam.coordinate + beam.direction
        return getBeamsFromCoordinate(nextCoordinate, beam.direction, schema)
    }


    override fun first(input: InputReader): Int {
        val schema = input.asLines()

        val startingChar = schema[0][0]
        val direction = when (startingChar) {
            '.' -> RIGHT
            '\\' -> BOTTOM
            '/' -> TOP
            else -> error("")
        }
        var beams = listOf(Beam(Coordinate(0, 0), direction))
        val visited = mutableSetOf<Beam>().apply { addAll(beams) }

        while (beams.isNotEmpty()) {
            beams = beams.flatMapTo(mutableSetOf()) { beam ->
                nextPosition(beam, schema)
            }.filter { visited.add(it) }
        }

        return visited.mapTo(mutableSetOf()) { it.coordinate }.count()
    }

    override fun second(input: InputReader): Int {
        val schema = input.asLines()

        val startingBeams = mutableListOf<Beam>()

        for (x in schema.indices) {
            val firstCoordinate = Coordinate(x, 0)
            startingBeams.addAll(getBeamsFromCoordinate(firstCoordinate, RIGHT, schema))
            val secondCoordinate = Coordinate(x, 0)
            startingBeams.addAll(getBeamsFromCoordinate(secondCoordinate, LEFT, schema))
        }

        for (y in schema.first().indices) {
            val firstCoordinate = Coordinate(0, y)
            startingBeams.addAll(getBeamsFromCoordinate(firstCoordinate, BOTTOM, schema))
            val secondCoordinate = Coordinate(schema.lastIndex, y)
            startingBeams.addAll(getBeamsFromCoordinate(secondCoordinate, TOP, schema))
        }

        return startingBeams.maxOf { startingBeam ->
            var beams = listOf(startingBeam)
            val visited = mutableSetOf<Beam>().apply { addAll(beams) }

            while (beams.isNotEmpty()) {
                beams = beams.flatMapTo(mutableSetOf()) { beam ->
                    nextPosition(beam, schema)
                }.filter { visited.add(it) }
            }

            visited.mapTo(mutableSetOf()) { it.coordinate }.count()
        }
    }

}

fun main() {
    Day16().solve()
}