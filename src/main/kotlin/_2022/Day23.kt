package _2022

import readInput

fun main() {
    fun parseElves(input: List<String>): Set<MutablePair<Int, Int>> {
        val elves = mutableSetOf<MutablePair<Int, Int>>()

        input.forEachIndexed { index, str ->
            str.forEachIndexed { strIndex, char ->
                if (char == '#')
                    elves.add(MutablePair(index, strIndex))
            }
        }

        return elves.toSet()
    }

    fun solve(input: List<String>, maxSteps: Int): Int {
        var elves = parseElves(input)

        val directions = listOf(
            setOf(Pair(-1, -1), Pair(-1, 0), Pair(-1, 1)),
            setOf(Pair(1, -1), Pair(1, 0), Pair(1, 1)),
            setOf(Pair(-1, -1), Pair(0, -1), Pair(1, -1)),
            setOf(Pair(-1, 1), Pair(0, 1), Pair(1, 1)),
        )

        for (step in 0 until maxSteps) {
            val futureSteps = mutableMapOf<MutablePair<Int, Int>, MutableList<MutablePair<Int, Int>>>()

            for (elf in elves) {
                val allPositions = directions.flatten().map {
                    MutablePair(elf.first + it.first, elf.second + it.second)
                }

                if (allPositions.intersect(elves).isEmpty()) {
                    continue
                }

                for (i in directions.indices) {
                    val direction = directions[(i + step).mod(directions.size)]
                    val positionsToCheck = direction.map {
                        MutablePair(elf.first + it.first, elf.second + it.second)
                    }

                    if (positionsToCheck.intersect(elves).isEmpty()) {
                        futureSteps.computeIfAbsent(positionsToCheck[1]) { mutableListOf() }.add(elf)
                        break
                    }
                }
            }

            val filterValues = futureSteps.filterValues { it.size == 1 }
            if (filterValues.isEmpty()) {
                return step + 1
            } else {
                filterValues.forEach { (position, elvesToMove) ->
                    elvesToMove.forEach {
                        it.first = position.first
                        it.second = position.second
                    }
                }
            }


            // recalculate all hashes
            elves = elves.toSet()
        }

        val minX = elves.minOf { it.first }
        val maxX = elves.maxOf { it.first }

        val minY = elves.minOf { it.second }
        val maxY = elves.maxOf { it.second }

        return (maxX - minX + 1) * (maxY - minY + 1) - elves.size
    }

    fun part1(input: List<String>): Int {
        return solve(input, 10)
    }


    fun part2(input: List<String>): Int {
        return solve(input, Int.MAX_VALUE)
    }

//     test if implementation meets criteria from the description, like:
    val testInput = readInput("Day23_test")
    println(part1(testInput))
    println(part2(testInput))

    val input = readInput("Day23")
    println(part1(input))
    println(part2(input))

}