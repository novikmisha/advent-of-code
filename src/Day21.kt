fun main() {


    fun dfs(
        monkeyName: String,
        monkeyMap: Map<String, MathMonkey>
    ): Long {
        val currentMonkey = monkeyMap[monkeyName]!!
        if (currentMonkey.simpleNumber > -1L) {
            return currentMonkey.simpleNumber
        }

        val firstMonkeyValue = dfs(currentMonkey.firstMonkey!!, monkeyMap)
        val secondMonkeyValue = dfs(currentMonkey.secondMonkey!!, monkeyMap)

        return currentMonkey.operation!!.invoke(firstMonkeyValue, secondMonkeyValue)
    }

    fun isInFirstChain(
        monkeyName: String,
        endMonkeyName: String,
        monkeyMap: Map<String, MathMonkey>
    ): Boolean {
        val currentMonkey = monkeyMap[monkeyName]!!
        val parentMonkey = monkeyMap.values.find { it.firstMonkey == monkeyName || it.secondMonkey == monkeyName }!!
        if (parentMonkey.name == endMonkeyName) {
            return parentMonkey.firstMonkey == currentMonkey.name
        }

        return isInFirstChain(parentMonkey.name, endMonkeyName, monkeyMap)
    }

    fun parseInput(input: List<String>): Map<String, MathMonkey> {
        return input.associate {
            val (name, operation) = it.split(":")
            if (operation.trim().toLongOrNull() != null) {
                name to MathMonkey(name, operation.trim().toLong(), null, null, null)
            } else {
                val (firstMonkey, operator, secondMonkey) = operation.trim()
                    .split(" ")

                var mathOperation: ((Long, Long) -> Long)? = null
                when (operator) {
                    "+" -> mathOperation = Long::plus
                    "-" -> mathOperation = Long::minus
                    "*" -> mathOperation = Long::times
                    "/" -> mathOperation = Long::div
                }

                name to MathMonkey(name, -1, mathOperation, firstMonkey, secondMonkey)
            }
        }
    }

    fun part1(input: List<String>): Long {
        val monkeyMap = parseInput(input)
        return dfs("root", monkeyMap)
    }

    fun solveEquation(
        endMonkey: String,
        startMonkey: String,
        endValue: Long,
        monkeyMap: Map<String, MathMonkey>
    ): Long {
        println("$endValue")
        if (startMonkey == endMonkey) {
            return endValue
        }
        val currentMonkey = monkeyMap[startMonkey]!!
        val inFirstChain = isInFirstChain(endMonkey, startMonkey, monkeyMap)
        var value = if (inFirstChain) {
            dfs(currentMonkey.secondMonkey!!, monkeyMap)
        } else {
            dfs(currentMonkey.firstMonkey!!, monkeyMap)
        }

        val operation = currentMonkey.operation!!
        val plusOperation: (Long, Long) -> Long = Long::plus
        val minusOperation: (Long, Long) -> Long = Long::minus
        val timesOperation: (Long, Long) -> Long = Long::times
        val divOperation: (Long, Long) -> Long = Long::div
        var result = 0L
        when (operation) {
            plusOperation -> result = endValue - value
            minusOperation -> result = if (inFirstChain) {endValue + value} else { value - endValue }
            timesOperation -> result = endValue / value
            divOperation -> result = if (inFirstChain) {endValue * value} else {value / endValue}
        }

        return if (inFirstChain) {
            solveEquation(endMonkey, currentMonkey.firstMonkey!!, result, monkeyMap)
        } else {
            solveEquation(endMonkey, currentMonkey.secondMonkey!!, result, monkeyMap)
        }
    }

    fun part2(input: List<String>): Long {
        val monkeyMap = parseInput(input)

        val rootMonkey = monkeyMap["root"]!!
        val inFirstChain = isInFirstChain("humn", "root", monkeyMap)
        val endValue: Long = if (inFirstChain) {
            dfs(rootMonkey.secondMonkey!!, monkeyMap)
        } else {
            dfs(rootMonkey.firstMonkey!!, monkeyMap)
        }

        return if (inFirstChain) {
            solveEquation("humn", rootMonkey.firstMonkey!!, endValue, monkeyMap)
        } else {
            solveEquation("humn", rootMonkey.secondMonkey!!, endValue, monkeyMap)
        }
    }

//     test if implementation meets criteria from the description, like:
    val testInput = readInput("Day21_test")
    println(part1(testInput))
    println(part2(testInput))

    val input = readInput("Day21")
    println(part1(input))
    println(part2(input))

}

data class MathMonkey(
    val name: String,
    var simpleNumber: Long,
    val operation: ((firstMonkey: Long, secondMonkey: Long) -> Long)?,
    val firstMonkey: String?,
    val secondMonkey: String?

)
