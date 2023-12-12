package _2023

import Day
import InputReader

class Day12 : Day(2023, 12) {

    data class Hash(
        val schema: String,
        val groups: String
    )

    override val firstTestAnswer = 21L
    override val secondTestAnswer = 525152L

    private fun solve(
        schema: String,
        neededGroups: List<Int>
    ) = solve(schema, neededGroups, mutableMapOf())

    private fun process(schema: String, neededGroups: List<Int>, hash: MutableMap<Hash, Long>): Long {
        if (neededGroups.isEmpty()) {
            return 0L
        }

        val currentGroup = neededGroups.first()
        if (schema.length < currentGroup) {
            return 0L
        }
        for (i in 1 until currentGroup) {
            if (schema[i] == '.') {
                return 0L
            }
        }

        if (currentGroup == schema.length) {
            if (neededGroups.size == 1) {
                return 1L
            }
            return 0L
        }

        if (schema[currentGroup] == '#') {
            return 0L
        }

        return solve(schema.drop(currentGroup + 1), neededGroups.drop(1), hash)
    }

    private fun solve(schema: String, neededGroups: List<Int>, hash: MutableMap<Hash, Long>): Long =
        hash.getOrPut(Hash(schema, neededGroups.joinToString { "," })) {
            if (schema.isEmpty()) {
                return@getOrPut if (neededGroups.isEmpty()) {
                    1L
                } else {
                    0L
                }
            }

            when (schema.first()) {
                '.' -> {
                    solve(schema.drop(1), neededGroups, hash)
                }

                '#' -> {
                    process(schema, neededGroups, hash)
                }

                '?' -> {
                    solve(schema.drop(1), neededGroups, hash) + process(schema, neededGroups, hash)
                }

                else -> {
                    error("")
                }
            }
        }

    override fun first(input: InputReader) = input.asLines().sumOf { line ->
        val (unparsedGroups, unparsedNeededGroups) = line.split(" ")
        val neededGroups = unparsedNeededGroups.trim().split(",").map { it.trim().toInt() }
        val schema = unparsedGroups.split(".").filter { it.isNotEmpty() }.joinToString(separator = ".")

        solve(schema, neededGroups)
    }


    override fun second(input: InputReader) = input.asLines().sumOf { line ->
        val (unparsedGroups, unparsedNeededGroups) = line.split(" ")

        val neededGroups =
            ("${unparsedNeededGroups.trim()},").repeat(5).trim().split(",").filter { it.isNotEmpty() }
                .map { it.trim().toInt() }

        val schema = ("${unparsedGroups.trim()}?").repeat(5).dropLast(1).split(".").filter { it.isNotEmpty() }
            .joinToString(separator = ".")

        solve(schema, neededGroups)
    }
}

fun main() {
    Day12().solve()
}