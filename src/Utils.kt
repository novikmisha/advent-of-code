import java.io.File
import java.math.BigInteger
import java.security.MessageDigest

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = File("src", "$name.txt")
    .readLines()

fun readInputAsLine(name: String) = File("src", "$name.txt")
    .readText()

fun readGroupedInput(name: String): List<List<String>> {
    val lines = File("src", "$name.txt")
        .readLines()

    val groupedLines = mutableListOf<List<String>>();
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
