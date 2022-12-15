fun main() {

    fun printMap(map: Map<Pair<Int, Int>, Item>) {
        val minX = map.keys.minBy { it.first }.first - 1
        val maxX = map.keys.maxBy { it.first }.first + 1
        val minY = 0
        val maxY = map.keys.maxBy { it.second }.second + 1

        for (y in minY..maxY) {
            for (x in minX..maxX) {
                print(map.getOrDefault(Pair(x, y), Item.AIR).mapChar)
            }

            println()
        }
    }

    fun buildRockMap(input: List<String>): MutableMap<Pair<Int, Int>, Item> {
        val rockMap = mutableMapOf<Pair<Int, Int>, Item>()
        input.forEach {
            for (rockPair in it.split(" -> ")
                .map { singleRockLine ->
                    singleRockLine.split(",").map(String::toInt)
                }.windowed(2)) {
                val (firstRock, secondRock) = rockPair.sortedBy { rock -> rock[0] }
                for (x in firstRock[0]..secondRock[0]) {
                    val (firstRock, secondRock) = rockPair.sortedBy { rock -> rock[1] }
                    for (y in firstRock[1]..secondRock[1]) {
                        rockMap[Pair(x, y)] = Item.ROCK
                    }
                }

            }
        }
        return rockMap
    }

    fun getNextSandPosition(sand: Pair<Int, Int>, rockMap: MutableMap<Pair<Int, Int>, Item>): Pair<Int, Int> {
        var flag = true
        val maxY = rockMap.keys.maxBy { it.second }.second + 1
        var result = Pair(sand.first, sand.second)
        while (flag) {
            if (result.second == maxY) {
                return result
            }
            var maybeNextPosition = Pair(result.first, result.second + 1)
            if (!rockMap.containsKey(maybeNextPosition)) {
                result = maybeNextPosition
                continue
            }
            maybeNextPosition = Pair(result.first - 1, result.second + 1)
            if (!rockMap.containsKey(maybeNextPosition)) {
                result = maybeNextPosition
                continue
            }
            maybeNextPosition = Pair(result.first + 1, result.second + 1)
            if (!rockMap.containsKey(maybeNextPosition)) {
                result = maybeNextPosition
                continue
            }

            flag = false
        }
        return result
    }

    fun part1(input: List<String>): Int {
        val rockMap = buildRockMap(input)

        var flag = true
        var count = 0
        val maxY = rockMap.keys.maxBy { it.second }.second + 1
        while (flag) {
            count++
            var sand = Pair(500, 0)
            val nextSandPosition = getNextSandPosition(sand, rockMap)
            sand = nextSandPosition
            if (sand.second >= maxY) {
                flag = false
            }
            rockMap[sand] = Item.SAND
//            printMap(rockMap)
        }
        return count - 1
    }

    fun part2(input: List<String>): Int {
        val rockMap = buildRockMap(input)
        val maxY = rockMap.keys.maxBy { it.second }.second + 2
        val minX = rockMap.keys.minBy { it.first }.first - 400
        val maxX = rockMap.keys.maxBy { it.first }.first + 400
        for (x in minX..maxX) {
            rockMap[Pair(x, maxY)] = Item.ROCK
        }

        var flag = true
        var count = 0
        while (flag) {
            count++
            var sand = Pair(500, 0)
            val nextSandPosition = getNextSandPosition(sand, rockMap)
            if (sand.first == nextSandPosition.first && sand.second == nextSandPosition.second) {
                flag = false
            }
            sand = nextSandPosition

            rockMap[sand] = Item.SAND
//            printMap(rockMap)
        }
        return count
    }


//     test if implementation meets criteria from the description, like:
    val testInput = readInput("Day14_test")
    println(part1(testInput))
    println(part2(testInput))

    val input = readInput("Day14")
    println(part1(input))
    println(part2(input))
}

enum class Item(val mapChar: Char) {
    ROCK('#'),
    SAND('0'),
    AIR('.')
}