fun main() {
    fun part1(input: List<List<String>>): Int {
        return input.maxOf { it.sumOf { str -> str.toInt() } }
    }

    fun part2(input: List<List<String>>): Int {
        return input.map { it.sumOf { str -> str.toInt() } }
            .sortedDescending()
            .take(3)
            .sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readGroupedInput("Day01_test")
    println(part1(testInput))
    println(part2(testInput))

    val input = readGroupedInput("Day01")
    println(part1(input))
    println(part2(input))
}
