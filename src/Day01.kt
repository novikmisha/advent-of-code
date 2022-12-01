fun main() {
    fun part1(input: List<List<String>>): Int {
        return input.maxOf { it.sumOf { str -> str.toInt() } }
    }

    fun part2(input: List<List<String>>): Int {
        val topGnomesNumber = 3

        val topGnomes = IntArray(topGnomesNumber)

        for (group in input) {
            var currentGnomeCalories = group.sumOf { it.toInt() }

            topGnomes.forEachIndexed { index, value ->
                if (value < currentGnomeCalories) {
                    topGnomes[index] = currentGnomeCalories.also { currentGnomeCalories = topGnomes[index] }
                }
            }
        }

        return topGnomes.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readGroupedInput("Day01_test")
    println(part1(testInput))

    val input = readGroupedInput("Day01")
    println(part1(input))
    println(part2(input))
}
