import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readLines
import kotlin.io.path.readText

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = Path("src/$name.txt")
    .readLines()

fun readInputAsLine(name: String) = Path("src/$name.txt")
    .readText()

fun readGroupedInput(name: String): List<List<String>> {
    val lines = readInput(name)

    val groupedLines = mutableListOf<List<String>>()
    var currentGroup = mutableListOf<String>()

    for (line in lines) {
        if (line.isBlank()) {
            groupedLines.add(currentGroup)
            currentGroup = mutableListOf()
        } else {
            currentGroup.add(line)
        }
    }

    groupedLines.add(currentGroup)

    return groupedLines
}

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

fun getCoordinatesAround(coordinates: List<Coordinate>) =
    mutableSetOf<Coordinate>().also {
        coordinates.forEach { coordinate ->
            for (dx in -1..1) {
                for (dy in -1..1) {
                    it.add(coordinate + Coordinate(dx, dy))
                }
            }
        }

        it.removeAll(coordinates.toSet())
    }


fun gcd(first: Long, second: Long): Long {
    var a = first
    var b = second

    while (b != 0L) {
        a = b.also {
            b = a.mod(b)
        }
    }

    return a
}


fun lcm(first: Long, second: Long): Long {
    return first * second / gcd(first, second);
}

