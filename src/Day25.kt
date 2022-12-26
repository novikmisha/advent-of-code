import kotlin.math.pow

fun main() {

    fun getNumber(char: Char): Long {
        return when (char) {
            '2' -> 2
            '1' -> 1
            '0' -> 0
            '-' -> -1
            '=' -> -2
            else -> throw RuntimeException("unknown char")
        }
    }

    fun toDecimal(str: String): Long {
        return str.reversed()
            .foldIndexed(0L) { index, acc, char -> (acc + (5.toDouble()).pow(index.toDouble()) * getNumber(char)).toLong() }
    }

    fun toSnafu(number: Long): String {
        val snafuNumbers = "012=-"
        var result = ""

        var workingNumber = number
        while (workingNumber != 0L) {
            val mod = (workingNumber).mod(5)
            result += snafuNumbers[mod]

            val carriedBase = when(mod) {
                3 -> 2
                4 -> 1
                else -> 0
            }

            workingNumber = (workingNumber + carriedBase) / 5
        }

        return result.reversed()
    }


    fun part1(input: List<String>): String {
        val sum = input.sumOf {
            toDecimal(it)
        }
        return toSnafu(sum)
    }

    fun part2(input: List<String>): Int {
        return 0
    }

//     test if implementation meets criteria from the description, like:
    val testInput = readInput("Day25_test")
    println(part1(testInput))
    println(part2(testInput))

    val input = readInput("Day25")
    println(part1(input))
    println(part2(input))

}