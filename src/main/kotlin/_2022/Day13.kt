package _2022

import readGroupedInput
import kotlin.math.max

fun main() {
    fun parseListFromString(str: String): List<Any> {
        val trimmedStr = str.removePrefix("[")
            .removeSuffix("]")
            .trim()

        val resultList = mutableListOf<Any>()
        var elementStartPosition = 0
        var arrayStartCounter = 0
        for (i in trimmedStr.indices) {
            if (trimmedStr[i] == '[') {
                arrayStartCounter += 1
            } else if (trimmedStr[i] == ']') {
                arrayStartCounter -= 1
                if (arrayStartCounter == 0) {
                    resultList.add(parseListFromString(trimmedStr.substring(elementStartPosition, i + 1)))
                    elementStartPosition = i + 2
                }
            } else if (trimmedStr[i] != ',') {
                continue
            } else {
                if (arrayStartCounter == 0 && i > elementStartPosition) {
                    resultList.add(trimmedStr.substring(elementStartPosition, i).toInt())
                    elementStartPosition = i + 1
                }
            }
        }
        if (elementStartPosition < trimmedStr.length) {
            resultList.add(trimmedStr.substring(elementStartPosition, trimmedStr.length).toInt())
        }
        return resultList
    }

    fun getValueAsList(second: Any?): List<*> {
        val rightAsList: List<*> = if (second is List<*>) {
            second
        } else {
            listOf(second)
        }
        return rightAsList
    }

    fun leftIsSmaller(left: List<*>, right: List<*>): Int {
        val maxArraySize = max(left.size, right.size)
        for (pair in left.take(maxArraySize).zip(right.take(maxArraySize))) {
            val first = pair.first
            val second = pair.second
            val areNotInts = first !is Int || second !is Int
            if (areNotInts) {

                val leftAsList: List<*> = getValueAsList(first)
                val rightAsList: List<*> = getValueAsList(second)

                val leftIsSmaller = leftIsSmaller(leftAsList, rightAsList)
                if (leftIsSmaller != 0) {
                    return leftIsSmaller
                }

            } else if ((first as Int) != (second as Int)) {
                val compareIntResult = (first - second).coerceIn(-1..1)
                if (compareIntResult != 0) {
                    return compareIntResult
                }
            }
        }

        return (left.size - right.size).coerceIn(-1..1)
    }

    fun part1(input: List<List<String>>): Int {
        val result = mutableListOf<Int>()
        input.forEachIndexed { pairIndex, pair ->

            val (left, right) = pair.map { parseListFromString(it) }

            val leftIsSmaller = leftIsSmaller(left, right)
            if (leftIsSmaller == -1) {
                result.add(pairIndex + 1)
            }

        }
        return result.sum()
    }

    fun part2(input: List<List<String>>): Int {
        val firstFlag = parseListFromString("[[2]]")
        val secondFlag = parseListFromString("[[6]]")

        val list = mutableListOf(firstFlag, secondFlag)
        list.addAll(input.flatten().map { parseListFromString(it) })

        val sortedList = list.sortedWith { first: List<*>, second: List<*> -> leftIsSmaller(first, second) }

        return (sortedList.indexOf(firstFlag) + 1) * (sortedList.indexOf(secondFlag) + 1)
    }


//     test if implementation meets criteria from the description, like:
    val testInput = readGroupedInput("Day13_test")
    println(part1(testInput))
    println(part2(testInput))

    val input = readGroupedInput("Day13")
    println(part1(input))
    println(part2(input))
}

