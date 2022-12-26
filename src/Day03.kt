fun main() {

    fun findCommonChar(strings: List<String>): Collection<Char> {
        return strings.map { it.toSet() }
            .reduce(Set<Char>::intersect)
    }

    fun getCharPriority(it: Char): Int {
        var result = (it - 'a') + 1
        if (result < 0) {
            result += 58
        }
        return result
    }

    fun part1(input: List<String>): Int {
        return input.sumOf {
            val chunked = it.chunked(it.length / 2)

            val chars = findCommonChar(chunked)

            chars.map(::getCharPriority).sum()
        }
    }

    fun part2(input: List<String>): Int {
        return input.chunked(3).sumOf {
            val chars = findCommonChar(it)
            chars.map(::getCharPriority).sum()
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    println(part1(testInput))
    println(part2(testInput))

    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))
}
