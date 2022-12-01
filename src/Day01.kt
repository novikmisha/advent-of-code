fun main() {
    fun part1(input: List<List<String>>): Int {
        var maxCalories = 0;

        for (group in input) {
            maxCalories = maxOf(maxCalories, group.sumOf { it.toInt() })
        }

        return maxCalories;
    }

    fun part2(input: List<List<String>>): Int {
        // can sort whole list at the end and take top3
        // dunno which way is faster
        val topGnomesNumber = 3;

        val topGnomes = IntArray(topGnomesNumber);

        for (group in input) {
            var currentGnomeCalories = group.sumOf { it.toInt() }

            topGnomes.forEachIndexed { index, value ->
                if (value < currentGnomeCalories) {
                    topGnomes[index] = currentGnomeCalories;
                    currentGnomeCalories = value;
                }
            }
        }

        return topGnomes.sum();
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readGroupedInput("Day01_test")
    println(part1(testInput))

    val input = readGroupedInput("Day01")
    println(part1(input))
    println(part2(input))
}
