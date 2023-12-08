package _2023

import Day
import InputReader
import lcm

class Day08 : Day(2023, 8) {

    override val firstTestAnswer = 6
    override val secondTestAnswer = 6L


    override fun first(input: InputReader): Int {
        val (unparsedSteps, unparsedMap) = input.asGroups()
        val steps = unparsedSteps.first()

        val map = unparsedMap.associate {
            val (currentNode, path) = it.split(" = ")
            val (left, right) = path.replace(")", "").replace("(", "").split(", ")

            currentNode.trim() to Pair(left.trim(), right.trim())
        }

        var step = 0
        var currentNode = "AAA"

        while (currentNode != "ZZZ") {
            val direction = steps[step.mod(steps.length)]
            currentNode = if (direction == 'R') {
                map[currentNode]!!.second
            } else {
                map[currentNode]!!.first
            }
            step++
        }
        return step
    }

    override fun second(input: InputReader): Long {
        val (unparsedSteps, unparsedMap) = input.asGroups()
        val steps = unparsedSteps.first()

        val map = unparsedMap.associate {
            val (currentNode, path) = it.split(" = ")
            val (left, right) = path.replace(")", "").replace("(", "").split(", ")

            currentNode.trim() to Pair(left.trim(), right.trim())
        }

        val currentNodes = map.keys.filter { it.endsWith('A') }

        val currentNodeSteps = currentNodes.map { startNode ->
            var step = 0L
            var currentNode = startNode
            while (!currentNode.endsWith('Z')) {
                val direction = steps[step.mod(steps.length)]
                currentNode = if (direction == 'R') {
                    map[currentNode]!!.second
                } else {
                    map[currentNode]!!.first
                }
                step++
            }
            step
        }

        return currentNodeSteps.reduce { result, currentValue -> lcm(result, currentValue) }
    }
}

fun main() {
    Day08().solve(skipTest = true)
}