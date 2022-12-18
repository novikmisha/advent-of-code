fun main() {
    fun getNearCoords(coord: Triple<Int, Int, Int>): Set<Triple<Int, Int, Int>> {
        return setOf(
            Triple(coord.first + 1, coord.second, coord.third),
            Triple(coord.first - 1, coord.second, coord.third),
            Triple(coord.first, coord.second + 1, coord.third),
            Triple(coord.first, coord.second - 1, coord.third),
            Triple(coord.first, coord.second, coord.third + 1),
            Triple(coord.first, coord.second, coord.third - 1),
        )
    }

    fun part1(input: List<String>): Int {
        val coords = input.map {
            val (x, y, z) = it.split(",")
                .map(String::toInt)
            Triple(x, y, z)
        }.toSet()

        var result = 0
        for (coord in coords) {
            result += 6 - getNearCoords(coord).intersect(coords).size
        }


        return result
    }

    fun part2(input: List<String>): Int {
        val coords = input.map {
            val (x, y, z) = it.split(",")
                .map(String::toInt)
            Triple(x, y, z)
        }.toSet()

        val maxX = coords.maxOf { it.first + 1 }
        val maxY = coords.maxOf { it.second + 1 }
        val maxZ = coords.maxOf { it.third + 1 }

        val minX = coords.minOf { it.first - 1 }
        val minY = coords.minOf { it.second - 1 }
        val minZ = coords.minOf { it.third - 1 }

        var result = 0
        val queue = ArrayDeque<Triple<Int, Int, Int>>()
        val visited = mutableSetOf<Triple<Int, Int, Int>>()
        queue.add(Triple(minX, minY, minZ))

        while (queue.isNotEmpty()) {
            val coord = queue.removeFirst()
            getNearCoords(coord)
                .filter {
                    it.first in minX..maxX
                            && it.second in minY..maxY
                            && it.third in minZ..maxZ
                }.forEach {
                    if (coords.contains(it)) {
                        result++
                    } else if (!visited.contains(it)) {
                        queue.remove(it)
                        queue.add(it)
                    }
                }
            visited.add(coord)
        }

        return result
    }


//     test if implementation meets criteria from the description, like:
    val testInput = readInput("Day18_test")
    println(part1(testInput))
    println(part2(testInput))

    val input = readInput("Day18")
    println(part1(input))
    println(part2(input))
}