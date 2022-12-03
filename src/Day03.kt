fun main() {

    fun findCommonChar(strings: List<String>): Collection<Char> {
        val chars = mutableMapOf<Char, Int>()
        strings.forEach {
            val charSet = it.toSet()
            charSet.forEach { char ->
                chars[char] = chars.computeIfAbsent(char) { 0 }.inc()

            }
        }
        return chars.filterValues { it == strings.size }
            .keys
    }

    fun part1(input: List<String>): Int {
        return input.map {
            val chunked = it.chunked(it.length / 2)

            val chars = findCommonChar(chunked)

            chars.map {
                var result = (it - 'a') + 1
                if (result < 0) {
                    result += 58
                }
                result
            }.sum()
        }.sum()
    }

    fun part2(input: List<String>): Int {
        return input.chunked(3).map {
            val chars = findCommonChar(it)
            chars.map {
                var result = (it - 'a') + 1
                if (result < 0) {
                    result += 58
                }
                result
            }.sum()
        }.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    println(part1(testInput))

    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))
}
