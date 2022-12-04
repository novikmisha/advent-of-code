fun main() {
    fun toRanges(it: String) = it.split(",")
        .map { range ->
            val splitRange = range.split("-")
            val start = splitRange[0].toInt()
            val end = splitRange[1].toInt()
            (start..end).toList()
        }


    fun part1(input: List<String>): Int {
        return input.map(::toRanges).count {
            val first = it[0]
            val second = it[1]
            val minSize = minOf(first.size, second.size)
            first.intersect(second).size == minSize
        }
    }

    fun part2(input: List<String>): Int {
        return input.map(::toRanges).count {
            val first = it[0]
            val second = it[1]
            first.intersect(second).isNotEmpty()
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    println(part1(testInput))

    val input = readInput("Day04")
    println(part1(input))
    println(part2(input))
}
