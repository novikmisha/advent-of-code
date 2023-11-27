package _2022

import readInput

fun main() {

    val directions = listOf(
        Pair(0, 1), //right
        Pair(1, 0), // bottom
        Pair(0, -1), // left
        Pair(-1, 0), // top
        Pair(0, 0), // current position
    )


    data class Blizzard(val position: Pair<Int, Int>, val direction: Pair<Int, Int>)

    data class PlayerState(val step: Int, val position: Pair<Int, Int>)

    fun buildBlizzardsOnStep(blizzards: Set<Blizzard>, maxX: Int, maxY: Int): Set<Blizzard> {
        return blizzards.map {
            var first = it.position.first + it.direction.first
            var second = it.position.second + it.direction.second
            if (first == 0) {
                first = maxX - 1
            }

            if (first == maxX) {
                first = 1
            }

            if (second == 0) {
                second = maxY - 1
            }

            if (second == maxY) {
                second = 1
            }

            Blizzard(Pair(first, second), it.direction)
        }.toSet()
    }

    fun solve(
        position: Pair<Int, Int>,
        endPosition: Pair<Int, Int>,
        maxX: Int,
        maxY: Int,
        stepToBlizzardsPosition: MutableMap<Int, Set<Blizzard>>,
        cycle: Int,
        startStep: Int
    ): PlayerState {
        val queue = ArrayDeque<PlayerState>()
        val seen = mutableSetOf<PlayerState>()
        queue.add(PlayerState(startStep, position))

        while (queue.isNotEmpty()) {
            val state = queue.removeFirst()
            val currentStep = state.step
            val currentPosition = state.position

            val currentBlizzards = stepToBlizzardsPosition.computeIfAbsent(currentStep.mod(cycle)) { step ->
                buildBlizzardsOnStep(
                    stepToBlizzardsPosition[step - 1]!!,
                    maxX,
                    maxY
                )
            }.toSet()

            val nextPositions = directions.map {
                Pair(it.first + currentPosition.first, it.second + currentPosition.second)
            }.toSet()

            if (endPosition in nextPositions) {
                return PlayerState(currentStep + 1, endPosition)
            }
            val currentBlizzardPositions = currentBlizzards.map { it.position }.toSet()

            val validNextPositions = nextPositions.filter {
                it == position || (it.first in (1 until maxX)
                        && it.second in (1 until maxY)
                        && it !in currentBlizzardPositions)
            }

            validNextPositions.forEach {
                val playerState = PlayerState(currentStep + 1, it)
                if (seen.add(playerState)) {
                    queue.add(playerState)
                }
            }
        }

        return PlayerState(-1, position)
    }

    fun part1(input: List<String>): Int {
        val blizzards = mutableListOf<Blizzard>()
        input.forEachIndexed { xIndex, str ->
            str.forEachIndexed { yIndex, char ->

                when (char) {
                    '>' -> blizzards.add(Blizzard(Pair(xIndex, yIndex), directions[0]))
                    '<' -> blizzards.add(Blizzard(Pair(xIndex, yIndex), directions[2]))
                    'v' -> blizzards.add(Blizzard(Pair(xIndex, yIndex), directions[1]))
                    '^' -> blizzards.add(Blizzard(Pair(xIndex, yIndex), directions[3]))
                }
            }
        }

        val maxX = input.size - 1
        val maxY = input.first().length - 1

        val cycle = input.size * input.first().length

        val stepToBlizzardsPosition = mutableMapOf(-1 to blizzards.toSet())

        val position = Pair(0, input.first().indexOf('.'))
        val endPosition = Pair(maxX, input.last().indexOf('.'))


        return solve(position, endPosition, maxX, maxY, stepToBlizzardsPosition, cycle, 0).step
    }

    fun part2(input: List<String>): Int {
        val blizzards = mutableListOf<Blizzard>()
        input.forEachIndexed { xIndex, str ->
            str.forEachIndexed { yIndex, char ->

                when (char) {
                    '>' -> blizzards.add(Blizzard(Pair(xIndex, yIndex), directions[0]))
                    '<' -> blizzards.add(Blizzard(Pair(xIndex, yIndex), directions[2]))
                    'v' -> blizzards.add(Blizzard(Pair(xIndex, yIndex), directions[1]))
                    '^' -> blizzards.add(Blizzard(Pair(xIndex, yIndex), directions[3]))
                }
            }
        }

        val maxX = input.size - 1
        val maxY = input.first().length - 1

        val cycle = input.size * input.first().length

        val stepToBlizzardsPosition = mutableMapOf(-1 to blizzards.toSet())

        val position = Pair(0, input.first().indexOf('.'))
        val endPosition = Pair(maxX, input.last().indexOf('.'))


        val first = solve(position, endPosition, maxX, maxY, stepToBlizzardsPosition, cycle, 0)
        val second = solve(endPosition, position, maxX, maxY, stepToBlizzardsPosition, cycle, first.step)
        return solve(position, endPosition, maxX, maxY, stepToBlizzardsPosition, cycle, second.step).step
    }

//     test if implementation meets criteria from the description, like:
    val testInput = readInput("Day24_test")
    println(part1(testInput))
    println(part2(testInput))

    val input = readInput("Day24")
    println(part1(input))
    println(part2(input))

}