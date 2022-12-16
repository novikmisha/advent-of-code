import java.math.BigInteger
import kotlin.math.abs

fun main() {

    fun printMap(map: Map<Pair<Int, Int>, BeaconItem>) {
        val minX = map.keys.minBy { it.first }.first - 1
        val maxX = map.keys.maxBy { it.first }.first + 1
        val minY = 0
        val maxY = map.keys.maxBy { it.second }.second + 1

        for (y in minY..maxY) {
            for (x in minX..maxX) {
                print(map.getOrDefault(Pair(x, y), BeaconItem.AIR).mapChar)
            }

            println()
        }
    }

    fun getCoordinates(str: String): Pair<Int, Int> {
        val cuttedStr = str.replace("Sensor at ", "")
            .replace("closest beacon is at", "")
            .trim()

        val (x, y) = cuttedStr.split(",")
            .map {
                it.replace("x=", "")
                    .replace("y=", "")
                    .trim()
                    .toInt()
            }

        return Pair(x, y)
    }

    fun getDistance(firstCord: Pair<Int, Int>, secondCord: Pair<Int, Int>) =
        abs(firstCord.first - secondCord.first) + abs(firstCord.second - secondCord.second)

    fun part1(input: List<String>, yRow: Int): Int {
        val beaconMap = mutableMapOf<Pair<Int, Int>, BeaconItem>()
        val scannedMap = mutableMapOf<Pair<Int, Int>, Int>()
        val sensors = mutableMapOf<Pair<Int, Int>, Int>()
        input.forEach {
            val (sensor, beacon) = it.split(":")
            val sensorCoord = getCoordinates(sensor)
            val beaconCoord = getCoordinates(beacon)
            val distance = getDistance(sensorCoord, beaconCoord)
            sensors[sensorCoord] = distance

            beaconMap[sensorCoord] = BeaconItem.SENSOR
            beaconMap[beaconCoord] = BeaconItem.BEACON
        }
        val minBy = sensors.minBy { it.key.first - it.value }
        val minX = minBy.key.first - minBy.value
        val maxBy = sensors.maxBy { it.key.first + it.value }
        val maxX = maxBy.key.first + maxBy.value

        var counter = 0
        for (i in minX..maxX) {
            val coord = Pair(i, yRow)
            if (beaconMap.getOrDefault(coord, BeaconItem.AIR) == BeaconItem.BEACON) {
                continue
            }
            val closeSensors = sensors.any {
                val distance = getDistance(it.key, coord)
                distance <= it.value
            }

            if (closeSensors) {
                counter++
            }
        }

//        printMap(beaconMap)
        return counter
    }

    fun part2(input: List<String>, maxCoord: Int): BigInteger? {
        val beaconMap = mutableMapOf<Pair<Int, Int>, BeaconItem>()
        val scannedMap = mutableMapOf<Pair<Int, Int>, Int>()
        val sensors = mutableMapOf<Pair<Int, Int>, Int>()
        input.forEach {
            val (sensor, beacon) = it.split(":")
            val sensorCoord = getCoordinates(sensor)
            val beaconCoord = getCoordinates(beacon)
            val distance = getDistance(sensorCoord, beaconCoord)
            sensors[sensorCoord] = distance

            beaconMap[sensorCoord] = BeaconItem.SENSOR
            beaconMap[beaconCoord] = BeaconItem.BEACON
        }

        val maxX = maxCoord
        val maxY = maxCoord

        for (sensor in sensors) {

            val pointsToCheck = mutableSetOf<Pair<Int, Int>>()

            (sensor.key.first..(sensor.key.first + sensor.value + 1))
                .zip((sensor.key.second - sensor.value - 1)..sensor.key.second)
                .forEach {
                    if ((0..maxX).contains(it.first) && (0..maxY).contains(it.second)) {
                        pointsToCheck.add(it)
                    }
                }

            ((sensor.key.first + sensor.value + 1) downTo sensor.key.first)
                .zip(sensor.key.second..(sensor.key.second + sensor.value + 1))
                .forEach {
                    if ((0..maxX).contains(it.first) && (0..maxY).contains(it.second)) {
                        pointsToCheck.add(it)
                    }
                }

            (sensor.key.first downTo (sensor.key.first - sensor.key.second - 1))
                .zip((sensor.key.second + sensor.value + 1) downTo sensor.key.second)
                .forEach {
                    if ((0..maxX).contains(it.first) && (0..maxY).contains(it.second)) {
                        pointsToCheck.add(it)
                    }
                }

            ((sensor.key.first - sensor.key.second - 1)..sensor.key.first)
                .zip(sensor.key.second downTo (sensor.key.second - sensor.value - 1))
                .forEach {
                    if ((0..maxX).contains(it.first) && (0..maxY).contains(it.second)) {
                        pointsToCheck.add(it)
                    }
                }

            for (coord in pointsToCheck) {
                if (beaconMap.getOrDefault(coord, BeaconItem.AIR) == BeaconItem.BEACON) {
                    continue
                }
                val closeSensors = sensors.all {
                    val distance = getDistance(it.key, coord)
                    distance > it.value
                }

                if (closeSensors) {
                    return coord.first.toBigInteger().multiply(4000000.toBigInteger()).plus(coord.second.toBigInteger())
                }
            }
        }



        return BigInteger.ZERO
    }


//     test if implementation meets criteria from the description, like:
    val testInput = readInput("Day15_test")
    println(part1(testInput, 10))
    println(part2(testInput, 20))

    val input = readInput("Day15")
    println(part1(input, 2000000))
    println(part2(input, 4000000))
}

enum class BeaconItem(val mapChar: Char) {
    BEACON('B'),
    SENSOR('S'),
    AIR('.')
}