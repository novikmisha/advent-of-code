package _2023

import Coordinate
import Day
import InputReader
import java.math.BigInteger
import java.util.*
import kotlin.collections.LinkedHashSet
import kotlin.math.abs
import kotlin.math.absoluteValue
import kotlin.time.measureTime

class Day18 : Day(2023, 18) {

    override val firstTestAnswer = 62
    override val secondTestAnswer = 952408144115

    val directionMap = mapOf(
        "U" to TOP,
        "D" to BOTTOM,
        "L" to LEFT,
        "R" to RIGHT
    )

    override fun first(input: InputReader): Int {
        val unparsedDirection = input.asLines().map {
            val (direction, steps) = it.split(" ")
            Pair(direction, steps.toInt())
        }

        val map = mutableMapOf<Int, MutableMap<Int, Coordinate>>()
        var currentCoordinate = Coordinate(0, 0)
        unparsedDirection.forEach { d ->
            val (directionChar, steps) = d
            val direction = directionMap[directionChar]!!

            repeat(steps) {
                map.getOrPut(currentCoordinate.x) { mutableMapOf() }[currentCoordinate.y] = direction
                currentCoordinate += direction
            }
        }


        val minRow = map.minOf { it.key }
        val maxRow = map.maxOf { it.key }
        val minColumn = map.minOf { it.value.minOf { row -> row.key } }
        val maxColumn = map.maxOf { it.value.maxOf { row -> row.key } }


        val firstInside = Coordinate(minRow - 1, minColumn)
        val queue: Queue<Coordinate> = LinkedList<Coordinate>().apply { add(firstInside) }
        val visited = mutableSetOf<Coordinate>()
        while (queue.isNotEmpty()) {
            val coordinate = queue.poll()
            if (!visited.add(coordinate)) {
                continue
            }
            directions.forEach { direction ->
                val nextCoordinate = coordinate + direction
                if (nextCoordinate.x in minRow - 1..maxRow + 1 && nextCoordinate.y in minColumn - 1..maxColumn + 1) {
                    if (map.getOrDefault(nextCoordinate.x, mutableMapOf())[nextCoordinate.y] == null) {
                        queue.add(nextCoordinate)
                    }
                }
            }
        }

        return (((maxRow + 2) - (minRow - 1)) * ((maxColumn + 2) - (minColumn - 1))) - visited.size
    }

    override fun second(input: InputReader): Long {
        val unparsedDirection = input.asLines().map {
            val hex = it.split(" ").last().drop(2).dropLast(1)

            val steps = hex.dropLast(1).toLong(radix = 16)
            val direction = when (hex.last()) {
                '0' -> RIGHT
                '1' -> BOTTOM
                '2' -> LEFT
                '3' -> TOP
                else -> error("")
            }

            Pair(direction, steps.toInt())
        }

        var perimeter = 0L
        var currentCoordinate = Coordinate(0, 0)
        val vertexes = mutableListOf<Coordinate>().apply { add(currentCoordinate) }
        println(measureTime {
            unparsedDirection.forEach { d ->
                val (direction, steps) = d
                currentCoordinate += Coordinate(direction.x * steps, direction.y * steps)
                vertexes.add(currentCoordinate)
                perimeter+=steps
            }
        })

        // https://en.wikipedia.org/wiki/Shoelace_formula
        // https://en.wikipedia.org/wiki/Pick%27s_theorem
        val numbers = vertexes.zipWithNext { first, second -> first.y.toLong() * second.x - first.x.toLong() * second.y }
        val area = numbers.sum() / 2
        return area + perimeter / 2 + 1
    }
}

fun main() {
    Day18().solve()
}