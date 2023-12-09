package _2023

import Day
import InputReader
import rangeIntersect

data class RangeToValue(
    val range: LongRange,
    val value: Long
)

class Day05 : Day(2023, 5) {

    override val firstTestAnswer: Long = 35L
    override val secondTestAnswer = 46L


    override fun first(input: InputReader): Long {
        val almanac = input.asGroups().toMutableList()

        val seeds = almanac.removeFirst().first()

        return numRegex.findAll(seeds).map {
            val seedId = it.value.toLong()
            almanac.fold(seedId) { result, map ->
                val range = map.drop(1).map { str ->
                    val (value, start, rangeSize) = str.trim().split(" ").map { value -> value.toLong() }
                    RangeToValue(start until start + rangeSize, value)
                }.firstOrNull { range -> range.range.contains(result) } ?: return@fold result
                result - range.range.first + range.value
            }
        }.min()
    }

    override fun second(input: InputReader): Long {
        val almanac = input.asGroups().toMutableList()

        val seeds = almanac.removeFirst().first()
        val seedsRanges =
            seeds.drop(7).split(" ").chunked(2)
                .map { it.first().trim().toLong() until it.first().toLong() + it.last().trim().toLong() }


        return seedsRanges.map { seedRange ->

            almanac.fold(listOf(seedRange)) { result, map ->

                var leftovers = result

                val ranges = map.drop(1).map { str ->
                    val (value, start, rangeSize) = str.trim().split(" ").map { value -> value.toLong() }
                    RangeToValue(start until start + rangeSize, value)
                }

                val finish = result.flatMap { currentRange ->

                    ranges.mapNotNullTo(mutableListOf()) { range ->
                        val intersect = (range.range rangeIntersect currentRange)
                            ?: return@mapNotNullTo null

                        if (intersect.isEmpty()) {
                            return@mapNotNullTo null
                        }

                        return@mapNotNullTo LongRange(
                            intersect.first - range.range.first + range.value,
                            intersect.last() - range.range.first + range.value
                        ).also {
                                leftovers = leftovers.flatMap { range ->
                                    val rangeIntersect =
                                        (range rangeIntersect intersect)
                                    if (rangeIntersect == null) {
                                        listOf(range)
                                    } else {
                                        val first = LongRange(range.first(), rangeIntersect.first() - 1L)
                                        val second = LongRange(rangeIntersect.last() + 1L, range.last())
                                        listOfNotNull(first, second).filter { !it.isEmpty() }
                                    }
                                }
                            }
                    }

                } + leftovers
                finish
            }.minOf {
                it.first
            }
        }.min()
    }
}

fun main() {
    Day05().solve(skipTest = true)
}