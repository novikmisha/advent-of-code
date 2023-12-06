import kotlin.reflect.KFunction1

abstract class Day(
    private val year: Int,
    private val day: Int
) {

    protected val testInput = InputReader(year, day, true)
    protected val input = InputReader(year, day, false)

    abstract val firstTestAnswer: Any
    abstract val secondTestAnswer: Any

    abstract fun first(input: InputReader): Any
    abstract fun second(input: InputReader): Any


    fun solve(
        skipTest: Boolean = false,
        skipFirst: Boolean = false,
        skipSecond: Boolean = false
    ) {
        println("✨Starting Day $year/$day")

        if (!skipFirst) {
            process(::first, skipTest, firstTestAnswer)
        }

        if (!skipSecond) {
            process(::second, skipTest, secondTestAnswer)
        }

        println("✨Day $year/$day done")
    }

    private fun process(solution: KFunction1<InputReader, Any>, skipTest: Boolean, testAnswer: Any) {

        if (!skipTest) {
            println("🧪Starting testing")
            val testResult = solution(testInput)

            if (testResult == testAnswer) {
                println("🥳Test successfully executed")
            } else {
                error("🤯Test failed with result $testResult")
            }
        }

        val result = solution(input)
        println("📝Got result: $result")
    }
}