package _2023

import Day
import InputReader

data class RangeToValue(
    val range: ULongRange,
    val value: ULong
)

class Day05 : Day(2023, 5) {

    override val firstTestAnswer: ULong = 35u
    override val secondTestAnswer = 0

    private val numRegex = Regex("\\d+")

    override fun first(input: InputReader): ULong {
        val almanac = input.asGroups().toMutableList()

        val seeds = almanac.removeFirst().first()
        return numRegex.findAll(seeds).map {
            val seedId = it.value.toULong()
            almanac.fold(seedId) { result, map ->
                val range = map.drop(1).map { str ->
                    val (value, start, rangeSize) = str.trim().split(" ").map { value -> value.toULong() }
                    RangeToValue(start until start + rangeSize, value)
                }.firstOrNull { range -> range.range.contains(result) } ?: return@fold result
                result - range.range.first + range.value
            }
        }.min()
    }

    override fun second(input: InputReader) = 0

}

fun main() {
    Day05().solve()
}