package _2023

import Day
import InputReader

class Day12 : Day(2023, 12) {


    override val firstTestAnswer = 21
    override val secondTestAnswer = 0

    private fun solve(
        schema: String,
        numberOfSprings: Int,
        passed: MutableSet<String>,
        visited: MutableSet<String>,
        neededGroups: List<Int>
    ) {
        if (!visited.add(schema)) {
            return
        }

        if (numberOfSprings == 0) {
            val groups = schema.replace("?", ".").split(".").filter { it.isNotEmpty() }

            if (groups.size != neededGroups.size) {
                return
            }

            if (groups.map { it.length }.zip(neededGroups).all { it.first == it.second }) {
                passed.add(schema)
            }

        } else {
            val replacePositions = schema.mapIndexedNotNull { index, char -> if (char == '?') index else null }
            for (replacePosition in replacePositions) {
                val newSchema = StringBuilder(schema).also { it.setCharAt(replacePosition, '#') }.toString()
                solve(newSchema, numberOfSprings - 1, passed, visited, neededGroups)
            }
        }
    }

    override fun first(input: InputReader) = input.asLines().sumOf { line ->
        val (unparsedGroups, unparsedNeededGroups) = line.split(" ")
        val neededGroups = unparsedNeededGroups.trim().split(",").map { it.trim().toInt() }
        val schema = unparsedGroups.split(".").filter { it.isNotEmpty() }.joinToString(separator = ".")

        val visited = mutableSetOf<String>()
        val passed = mutableSetOf<String>()
        val numberOfSprings = neededGroups.sum() - schema.count { it == '#' }
        solve(schema, numberOfSprings, passed, visited, neededGroups)

        passed.size
    }

    override fun second(input: InputReader) = input.asLines().sumOf { line ->
        val (unparsedGroups, unparsedNeededGroups) = line.split(" ")
        val neededGroups = ("${unparsedNeededGroups.trim()},").repeat(5).dropLast(1).split(",").map { it.trim().toInt() }
        val schema = ("${unparsedGroups.trim()}?").repeat(5).dropLast(1).split(".").filter { it.isNotEmpty() }.joinToString(separator = ".")
        println(neededGroups)
        println(schema)

        val visited = mutableSetOf<String>()
        val passed = mutableSetOf<String>()
        val numberOfSprings = neededGroups.sum() - schema.count { it == '#' }
        solve(schema, numberOfSprings, passed, visited, neededGroups)

        println("done")
        passed.size
    }
}

fun main() {
    Day12().solve(skipFirst = true)
}