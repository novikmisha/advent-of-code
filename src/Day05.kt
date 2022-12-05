fun main() {

    fun  processCargo(input: List<List<String>>, itemSort: (items: List<String>) -> List<String>): String {
        val cargo = input[0]
        val commands = input[1]

        val numberOfColumns = cargo[cargo.size - 1].split(" ")
            .filter(String::isNotEmpty)
            .maxOf { it.toInt() }

        val stacks = List<ArrayDeque<String>>(numberOfColumns) { ArrayDeque() }

        cargo.reversed().drop(1).forEach { items ->
            items.replace("[", " ").replace("]", " ")
                .split("   ")
                .forEachIndexed { itemColumn, item ->
                    if (item.isNotEmpty()) {
                        stacks[itemColumn].addLast(item.trim())
                    }
                }
        }

        for (command in commands) {
            val splittedCommand = command
                .split(" ")
                .mapNotNull(String::toIntOrNull)

            val itemCount = splittedCommand[0]
            val from = splittedCommand[1]
            val to = splittedCommand[2]

            val lastItems = stacks[from - 1].takeLast(itemCount)
            repeat(itemCount) { stacks[from - 1].removeLast() }
            stacks[to - 1].addAll(itemSort(lastItems))
        }

        return stacks.joinToString(separator = "") { it.last() }
    }

    fun part1(input: List<List<String>>): String {
        return processCargo(input) { it.reversed() }
    }

    fun part2(input: List<List<String>>): String {
        return processCargo(input) { it }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readGroupedInput("Day05_test")
    println(part1(testInput))
    println(part2(testInput))

    val input = readGroupedInput("Day05")
    println(part1(input))
    println(part2(input))
}
