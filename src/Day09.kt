import kotlin.math.abs

fun main() {
    fun tooFarAway(h: IntArray, t: IntArray): Boolean {
        return (abs(h[0] - t[0]) > 1) || (abs(h[1] - t[1]) > 1)
    }


    fun part1(input: List<String>): Int {
        val h = IntArray(2)
        var t = IntArray(2)

        val visitedPositions = mutableSetOf<Pair<Int, Int>>()
        for (command in input) {
            val (direction, steps) = command.split(" ")
            repeat(steps.toInt()) {
                val prevH = h.copyOf()
                when (direction) {
                    "R" -> h[0]++
                    "L" -> h[0]--
                    "U" -> h[1]++
                    "D" -> h[1]--
                }
                if (tooFarAway(h, t)) {
                    t = prevH
                }

                visitedPositions.add(Pair(t[0], t[1]))
            }
        }

        return visitedPositions.size
    }

    fun part2(input: List<String>): Int {
        val snake = MutableList(10) { IntArray(2) }
        val visitedPositions = mutableSetOf<Pair<Int, Int>>()

        for (command in input) {
            val (direction, steps) = command.split(" ")
            repeat(steps.toInt()) {
                val h = snake.first()

                when (direction) {
                    "R" -> h[0]++
                    "L" -> h[0]--
                    "U" -> h[1]++
                    "D" -> h[1]--
                }

                snake.forEachIndexed { index, body ->
                    if (index == 0) {
//                        return 0
                    } else {
                        val prevHead = snake[index - 1]
                        if (tooFarAway(body, prevHead)) {
                            val addX = prevHead[0] - body[0]
                            val addY = prevHead[1] - body[1]

                            body[0] += addX.coerceIn(-1..1)
                            body[1] += addY.coerceIn(-1..1)
                        }
                    }
                }


                val tail = snake.last()
                visitedPositions.add(Pair(tail[0], tail[1]))
            }
        }

        return visitedPositions.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09_test")
    println(part1(testInput))
    println(part2(testInput))

    val input = readInput("Day09")
    println(part1(input))
    println(part2(input))
}