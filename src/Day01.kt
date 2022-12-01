fun main() {
    fun part1(input: List<String>): Int {
        var maxCalories = 0;
        var currentGnomeCalories = 0;

        for (calories in input) {
            if (calories.isEmpty()) {
                if (currentGnomeCalories > maxCalories) {
                    maxCalories = currentGnomeCalories
                }

                currentGnomeCalories = 0
                continue
            } else {
                currentGnomeCalories += calories.toInt();
            }
        }

        if (currentGnomeCalories > maxCalories) {
            maxCalories = currentGnomeCalories
        }

        return maxCalories;
    }

    fun part2(input: List<String>): Int {
        val topGnomesNumber = 3;

        var topGnomes = IntArray(topGnomesNumber);
        var currentGnomeCalories = 0;

        for (calories in input) {
            if (calories.isEmpty()) {
                topGnomes.forEachIndexed { index, value ->
                    if (value < currentGnomeCalories) {
                        topGnomes[index] = currentGnomeCalories;
                        currentGnomeCalories = value;
                    }
                }
                currentGnomeCalories = 0
                continue
            } else {
                currentGnomeCalories += calories.toInt();
            }
        }

        topGnomes.forEachIndexed { index, value ->
            if (value < currentGnomeCalories) {
                topGnomes[index] = currentGnomeCalories;
                currentGnomeCalories = value;
            }
        }

        return topGnomes.sum();
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    println(part1(testInput))

    val input = readInput("Day01")
    println(part1(input))
    println(part2(input))
}
