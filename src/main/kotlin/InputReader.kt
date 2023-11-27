import java.lang.System.lineSeparator
import kotlin.io.path.Path
import kotlin.io.path.readLines
import kotlin.io.path.readText

class InputReader(
    private val year: Int,
    private val day: Int,
    private val isTest: Boolean
) {

    private val path: String = buildString {
        append("src/main/resources/$year/${day.toString().padStart(2, '0')}")
        if (isTest) {
            append("_test")
        }
        append(".txt")
    }

    private val file = Path(path)

    fun asLines() = file.readLines()
    fun asLine() = file.readText()
    fun asGroups() = asLine().split(lineSeparator() + lineSeparator()).map { it.split(lineSeparator()) }

}