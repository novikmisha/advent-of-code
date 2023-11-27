package _2022

import readInputAsLine

fun main() {

    fun buildLine(startHeight: Int): List<MutablePair<Int, Int>> {
        return listOf(
            MutablePair(2, startHeight),
            MutablePair(3, startHeight),
            MutablePair(4, startHeight),
            MutablePair(5, startHeight)
        )
    }

    fun buildCross(startHeight: Int): List<MutablePair<Int, Int>> {
        return listOf(
            MutablePair(3, startHeight),
            MutablePair(2, startHeight + 1),
            MutablePair(3, startHeight + 1),
            MutablePair(4, startHeight + 1),
            MutablePair(3, startHeight + 2)
        )
    }

    fun buildLShape(startHeight: Int): List<MutablePair<Int, Int>> {
        return listOf(
            MutablePair(2, startHeight),
            MutablePair(3, startHeight),
            MutablePair(4, startHeight),
            MutablePair(4, startHeight + 1),
            MutablePair(4, startHeight + 2)
        )
    }

    fun buildVerticalLine(startHeight: Int): List<MutablePair<Int, Int>> {
        return listOf(
            MutablePair(2, startHeight),
            MutablePair(2, startHeight + 1),
            MutablePair(2, startHeight + 2),
            MutablePair(2, startHeight + 3),
        )
    }

    fun buildSquare(startHeight: Int): List<MutablePair<Int, Int>> {
        return listOf(
            MutablePair(2, startHeight),
            MutablePair(3, startHeight),
            MutablePair(2, startHeight + 1),
            MutablePair(3, startHeight + 1),
        )
    }

    val figureMap = listOf(
        ::buildLine,
        ::buildCross,
        ::buildLShape,
        ::buildVerticalLine,
        ::buildSquare
    )

    fun getFigure(step: Long, towerHeight: Int): List<MutablePair<Int, Int>> {
        val figureNumber = (step % figureMap.size).toInt()
        return figureMap[figureNumber].invoke(towerHeight + 3)
    }

    fun moveFigureRight(figure: List<MutablePair<Int, Int>>, map: MutableList<MutableList<Boolean>>) {
        if (figure.all { it.first < 6 && !map.getOrElse(it.second) { _ -> List(7) { false } }[it.first + 1] }) {
            figure.forEach { it.first += 1 }
        }
    }

    fun moveFigureLeft(figure: List<MutablePair<Int, Int>>, map: MutableList<MutableList<Boolean>>) {
        if (figure.all { it.first > 0 && !map.getOrElse(it.second) { _ -> List(7) { false } }[it.first - 1] }) {
            figure.forEach { it.first -= 1 }
        }
    }

    fun moveFigureDown(figure: List<MutablePair<Int, Int>>) {
        figure.forEach { it.second -= 1 }
    }

    fun connectedToTower(map: MutableList<MutableList<Boolean>>, figure: List<MutablePair<Int, Int>>): Boolean {
        val closePoints = figure.filter { it.second < map.size }
        return closePoints.any { map[it.second][it.first] }
    }

    fun addFigureToMap(map: MutableList<MutableList<Boolean>>, figure: List<MutablePair<Int, Int>>) {
        val closePoints = figure.filter { it.second >= map.size }
        if (closePoints.isNotEmpty()) {
            val rowsToAdd = closePoints.maxOf { it.second - map.size }
            repeat(rowsToAdd + 1) {
                map.add(MutableList(7) { false })
            }
        }
        figure.forEach {
            map[it.second][it.first] = true
        }
    }

    fun part1(input: String): Int {
        val map = mutableListOf<MutableList<Boolean>>()
        map.add(MutableList(7) { true })

        var gasNumber = 0
        for (step in 0 until 2022L) {
            val figure = getFigure(step, map.size)
            var connectedToTower = false
            do {
                when (input[gasNumber]) {
                    '>' -> moveFigureRight(figure, map)
                    '<' -> moveFigureLeft(figure, map)
                }
                gasNumber = (gasNumber + 1) % input.length

                val nextFigure = figure.map { it.copy() }
                moveFigureDown(nextFigure)
                connectedToTower = connectedToTower(map, nextFigure)
                if (!connectedToTower) {
                    moveFigureDown(figure)
                }
            } while (!connectedToTower)

            addFigureToMap(map, figure)
        }

        return map.size - 1
    }

    fun part2(input: String): Long {
        var map = mutableListOf<MutableList<Boolean>>()
        map.add(MutableList(7) { true })

        val cycleToMapSize = mutableListOf<Int>()

        val cycles = 1000000000000
        var gasNumber = 0
        val findingCycleGas = mutableListOf<Int>()
        for (step in 0L until cycles) {
            val figure = getFigure(step, map.size)
            var connectedToTower: Boolean
            do {
                when (input[gasNumber]) {
                    '>' -> moveFigureRight(figure, map)
                    '<' -> moveFigureLeft(figure, map)
                }
                gasNumber = (gasNumber + 1) % input.length

                val nextFigure = figure.map { it.copy() }
                moveFigureDown(nextFigure)
                connectedToTower = connectedToTower(map, nextFigure)
                if (!connectedToTower) {
                    moveFigureDown(figure)
                }
            } while (!connectedToTower)

            addFigureToMap(map, figure)

            val nextStep = step + 1
            if (nextStep % figureMap.size.toLong() == 0L) {

                cycleToMapSize.add(map.size - 1)
                if (findingCycleGas.count { it == gasNumber } >= 2) {
                    val cycleStart = findingCycleGas.indexOf(gasNumber)
                    val cycleEnd = findingCycleGas.lastIndexOf(gasNumber)

                    val cycleLength = cycleEnd - cycleStart

                    val possibleCycle = findingCycleGas.takeLast(cycleLength)
                    val prevCycle = findingCycleGas.toList().dropLast(cycleLength).takeLast(cycleLength)

                    if (possibleCycle == prevCycle && (1000000000000 - step - 1) % cycleLength == 0L) {

                        val cycleMapSizeEnd = cycleToMapSize[cycleToMapSize.size - 1]
                        val cycleMapSizeStart = cycleToMapSize[cycleToMapSize.size - cycleLength - 1]
                        return (1000000000000 - step - 1) / cycleLength / figureMap.size * (cycleMapSizeEnd - cycleMapSizeStart) + cycleMapSizeEnd
                    }

                }
                findingCycleGas.add(gasNumber)
            }

        }

        return 0
    }


//     test if implementation meets criteria from the description, like:
    val testInput = readInputAsLine("Day17_test")
    println(part1(testInput))
    println(part2(testInput))

    val input = readInputAsLine("Day17")
    println(part1(input))
    println(part2(input))
}

data class MutablePair<T, U>(var first: T, var second: U)