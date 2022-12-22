import kotlin.math.absoluteValue

fun main() {
    val directions = listOf(
        Pair(0, 1), //right
        Pair(1, 0), // bottom
        Pair(0, -1), // left
        Pair(-1, 0), // top
    )

    fun getNextDirection(currentDirection: Pair<Int, Int>, rotate: Char): Pair<Int, Int> {
        return when (rotate) {
            'R' -> directions[(directions.indexOf(currentDirection) + 1).mod(4)]
            'L' -> directions[(directions.indexOf(currentDirection) - 1).mod(4).absoluteValue]
            else -> currentDirection
        }
    }

    fun isWall(map: List<String>, position: Pair<Int, Int>): Boolean {
        return map[position.first][position.second] == '#'
    }

    fun isAir(map: List<String>, position: Pair<Int, Int>): Boolean {
        return position.first < 0 || map.size <= position.first
                || position.second < 0 || map[position.first].length <= position.second
                || map[position.first][position.second] == ' '
    }

    fun part1(input: List<List<String>>): Int {
        val (map, movementList) = input
        var movementStr = movementList.first()
        var position = Pair(0, map.first().indexOf('.'))
        var direction = directions.first()

        while (movementStr.isNotEmpty()) {
            var indexOfRotation = movementStr.indexOfFirst { !it.isDigit() }
            if (indexOfRotation == -1) {
                indexOfRotation = movementStr.length - 1
            }

            val currentMovement = movementStr.substring(0, indexOfRotation + 1)
            val steps = currentMovement.takeWhile { it.isDigit() }.toInt()

            repeat(steps) {

                var nextPosition = Pair(
                    (position.first + direction.first).mod(map.size),
                    (position.second + direction.second).mod(map[position.first].length)
                )

                while (isAir(map, nextPosition)) {
                    nextPosition = Pair(
                        (nextPosition.first + direction.first).mod(map.size),
                        (nextPosition.second + direction.second).mod(map[position.first].length)
                    )
                }

                if (!isWall(map, nextPosition)) {
                    position = nextPosition
                }
            }

            if (!currentMovement.last().isDigit()) {
                direction = getNextDirection(direction, currentMovement.last())
            }

            movementStr = movementStr.drop(indexOfRotation + 1)

        }

        return 1000 * (position.first + 1) + 4 * (position.second + 1) + directions.indexOf(direction)
    }


    // value - top left corner of zone
    fun buildZonesFromMap(map: List<String>, step: Int): Map<Char, Pair<Int, Int>> {
        var key = 'a'
        val zones = mutableMapOf<Char, Pair<Int, Int>>()

        for (i in map.indices step step) {
            for (j in 0 until map[i].length step step) {

                if (!isAir(map, Pair(i, j))) {
                    zones[key] = Pair(i, j)
                    key++
                }
            }
        }

        return zones;
    }

    // todo hardcoded
    fun buildTeleports(
        zones: Map<Char, Pair<Int, Int>>,
        test: Boolean
    ): Map<Pair<Char, Pair<Int, Int>>, Pair<Char, Pair<Int, Int>>> {
        var right = directions[0]
        var bottom = directions[1]
        var left = directions[2]
        var top = directions[3]

        //        aaaa
        //        aaaa
        //        aaaa
        //        aaaa
        //bbbbccccdddd
        //bbbbccccdddd
        //bbbbccccdddd
        //bbbbccccdddd
        //        eeeeffff
        //        eeeeffff
        //        eeeeffff
        //        eeeeffff

        val teleports = mutableMapOf<Pair<Char, Pair<Int, Int>>, Pair<Char, Pair<Int, Int>>>()
        if (test) {
            teleports[Pair('a', right)] = Pair('f', left)
            teleports[Pair('a', top)] = Pair('b', bottom)
            teleports[Pair('a', left)] = Pair('c', bottom)

            teleports[Pair('b', top)] = Pair('a', bottom)
            teleports[Pair('b', left)] = Pair('f', top)
            teleports[Pair('b', bottom)] = Pair('e', top)

            teleports[Pair('c', top)] = Pair('a', right)
            teleports[Pair('c', bottom)] = Pair('f', right)

            teleports[Pair('d', right)] = Pair('f', bottom)

            teleports[Pair('e', left)] = Pair('c', top)
            teleports[Pair('e', bottom)] = Pair('b', top)

            teleports[Pair('f', top)] = Pair('d', left)
            teleports[Pair('f', right)] = Pair('a', left)
            teleports[Pair('f', bottom)] = Pair('b', right)

        } else {
            teleports[Pair('a', left)] = Pair('d', right)
            teleports[Pair('a', top)] = Pair('f', right)

            teleports[Pair('b', top)] = Pair('f', top)
            teleports[Pair('b', right)] = Pair('e', left)
            teleports[Pair('b', bottom)] = Pair('c', left)

            teleports[Pair('c', left)] = Pair('d', bottom)
            teleports[Pair('c', right)] = Pair('b', top)

            teleports[Pair('d', left)] = Pair('a', right)
            teleports[Pair('d', top)] = Pair('c', right)

            teleports[Pair('e', right)] = Pair('b', left)
            teleports[Pair('e', bottom)] = Pair('f', left)

            teleports[Pair('f', right)] = Pair('e', top)
            teleports[Pair('f', left)] = Pair('a', bottom)
            teleports[Pair('f', bottom)] = Pair('b', bottom)
        }

        return teleports
    }

    fun getZone(zones: Map<Char, Pair<Int, Int>>, position: Pair<Int, Int>, step: Int): Char {
        for (zone in zones) {
            val startPosition = zone.value
            if ((startPosition.first until startPosition.first + step).contains(position.first)
                && (startPosition.second until startPosition.second + step).contains(position.second)
            ) {
                return zone.key
            }
        }

        throw RuntimeException("zone not found")
    }

    fun part2(input: List<List<String>>, isTest: Boolean): Int {
        val (map, movementList) = input

        val step = if (isTest) {
            4
        } else {
            50
        }
        val zones = buildZonesFromMap(map, step)
        var teleports = buildTeleports(zones, isTest)

        var movementStr = movementList.first()
        var position = Pair(0, map.first().indexOf('.'))
        var direction = directions.first()
        var currentZone = 'a'

        val right = directions[0]
        val bottom = directions[1]
        val left = directions[2]
        val top = directions[3]

        while (movementStr.isNotEmpty()) {
            var indexOfRotation = movementStr.indexOfFirst { !it.isDigit() }
            if (indexOfRotation == -1) {
                indexOfRotation = movementStr.length - 1
            }

            val currentMovement = movementStr.substring(0, indexOfRotation + 1)
            val steps = currentMovement.takeWhile { it.isDigit() }.toInt()

            repeat(steps) {

                var nextPosition = Pair(
                    (position.first + direction.first),
                    (position.second + direction.second)
                )

                var nextDirection: Pair<Int, Int>? = null

                if (isAir(map, nextPosition)) {

                    val teleport = teleports[Pair(currentZone, direction)]!!

                    val currentPositionInZone = when (direction) {
                        top -> position.second
                        bottom -> step - position.second.mod(step) - 1
                        left -> step - position.first.mod(step) - 1
                        right -> position.first
                        else -> throw RuntimeException("unknown direction")
                    }.mod(step)


                    val nextZone = zones[teleport.first]!!
                    nextPosition = when (teleport.second) {
                        top -> Pair(nextZone.first + step - 1, nextZone.second + currentPositionInZone)
                        bottom -> Pair(nextZone.first, nextZone.second + (step - currentPositionInZone - 1))
                        left -> Pair(nextZone.first + (step - currentPositionInZone - 1), nextZone.second + step - 1)
                        right -> Pair(nextZone.first + currentPositionInZone, nextZone.second)
                        else -> throw RuntimeException("unknown direction")
                    }
                    nextDirection = teleport.second
                }

                if (!isWall(map, nextPosition)) {
                    position = nextPosition
                    currentZone = getZone(zones, position, step)
                    if (nextDirection != null) {
                        direction = nextDirection
                    }
                }
            }

            if (!currentMovement.last().isDigit()) {
                direction = getNextDirection(direction, currentMovement.last())
            }

            movementStr = movementStr.drop(indexOfRotation + 1)

        }

        return 1000 * (position.first + 1) + 4 * (position.second + 1) + directions.indexOf(direction)
    }

//     test if implementation meets criteria from the description, like:
    val testInput = readGroupedInput("Day22_test")
    println(part1(testInput))
    println(part2(testInput, true))

    val input = readGroupedInput("Day22")
    println(part1(input))
    println(part2(input, false))

}