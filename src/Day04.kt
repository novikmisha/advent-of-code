fun main() {
    fun toRanges(it: String) = it.split(",")
        .map { range ->
            val (start, end) = range.split("-").map(String::toInt)
            (start..end).toList()
        }


    fun part1(input: List<String>): Int {
        return input.map(::toRanges).count {
            val (first, second) = it
            val minSize = minOf(first.size, second.size)
            first.intersect(second.toSet()).size == minSize
        }
    }

    fun part2(input: List<String>): Int {
        return input.map(::toRanges).count {
            val (first, second) = it
            first.intersect(second.toSet()).isNotEmpty()
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    println(part1(testInput))
    println(part2(testInput))

    val input = readInput("Day04")
    println(part1(input))
    println(part2(input))
}
