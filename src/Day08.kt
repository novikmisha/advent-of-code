fun main() {
    fun part1(input: List<String>): Int {
        val intInput = input.map { it.map { char -> char.digitToInt() } }

        val max_i = intInput.size
        val max_j = intInput[0].size
        var visibleInside = 0
        for (i in 1 until max_i - 1) {
            for (j in 1 until max_j - 1) {
                val tree = intInput[i][j]

                var bottomFlag = true
                var rightFlag = true
                var topFlag = true
                var leftFlag = true
                for (k in i + 1 until max_i) {
                    if (intInput[k][j] >= tree) {
                        bottomFlag = false
                    }
                }
                for (k in j + 1 until max_j) {
                    if (intInput[i][k] >= tree) {
                        rightFlag = false
                    }
                }
                for (k in i - 1 downTo 0) {
                    if (intInput[k][j] >= tree) {
                        topFlag = false
                    }
                }
                for (k in j - 1 downTo 0) {
                    if (intInput[i][k] >= tree) {
                        leftFlag = false
                    }
                }

                if (listOf(bottomFlag, rightFlag, topFlag, leftFlag).any { it }) {
                    visibleInside++
                }
            }
        }

        return (max_i + max_j) * 2 - 4 + visibleInside
    }

    fun part2(input: List<String>): Int {
        val intInput = input.map { it.map { char -> char.digitToInt() } }
        val max_i = intInput.size
        val max_j = intInput[0].size
        var scenics = mutableListOf<Int>()
        for (i in 1 until max_i - 1) {
            for (j in 1 until max_j - 1) {
                val tree = intInput[i][j]

                var bottom = 0
                var right = 0
                var top = 0
                var left = 0
                for (k in i + 1 until max_i) {
                    bottom++
                    if (intInput[k][j] >= tree) {
                        break
                    }
                }
                for (k in j + 1 until max_j) {
                    right++
                    if (intInput[i][k] >= tree) {
                        break
                    }
                }
                for (k in i - 1 downTo 0) {
                    top++
                    if (intInput[k][j] >= tree) {
                        break
                    }
                }
                for (k in j - 1 downTo 0) {
                    left++
                    if (intInput[i][k] >= tree) {
                        break
                    }
                }

                scenics.add(bottom * right * top * left)
            }
        }

        return scenics.max()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test")
    println(part1(testInput))
    println(part2(testInput))

    val input = readInput("Day08")
    println(part1(input))
    println(part2(input))
}