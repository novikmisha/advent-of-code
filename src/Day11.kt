import java.math.BigInteger

fun main() {
    fun getNewItemLvl(item: BigInteger, operation: String): BigInteger {
        val (first, operationChar, second) = operation.replace("old", item.toString())
            .split(" ")

        when (operationChar) {
            "+" -> return first.toBigInteger() + second.toBigInteger()
            "-" -> return first.toBigInteger() - second.toBigInteger()
            "*" -> return first.toBigInteger() * second.toBigInteger()
            "/" -> return first.toBigInteger() / second.toBigInteger()
        }

        return BigInteger.ZERO
    }

    fun parseMonkey(
        unparsedMonkey: List<String>,
        monkeyMap: MutableMap<Int, Monkey>,
        monkeyIndex: Int
    ) {
        val items = ArrayDeque<BigInteger>()
        var operation = ""
        var divisible = 0
        var testTrueMonkeyNumber = 0
        var testFalseMonkeyNumber = 0

        unparsedMonkey.forEachIndexed { index, line ->
            when (index) {
                1 -> items.addAll(line.replace("Starting items: ", "")
                    .replace(",", "")
                    .split(" ")
                    .filter { it.isNotEmpty() }
                    .map { it.trim().toBigInteger() })

                2 -> {
                    operation = line.replace("  Operation: new = ", "")
                        .trim()
                }

                3 -> {
                    divisible = line.split(" ")
                        .firstNotNullOf { it.toIntOrNull() }
                }

                4 -> {
                    testTrueMonkeyNumber = line.split(" ")
                        .firstNotNullOf { it.toIntOrNull() }
                }

                5 -> {
                    testFalseMonkeyNumber = line.split(" ")
                        .firstNotNullOf { it.toIntOrNull() }
                }
            }
        }

        val monkey = Monkey(items, operation, divisible, testTrueMonkeyNumber, testFalseMonkeyNumber)
        monkeyMap[monkeyIndex] = monkey
    }

    fun catchMonkeysItems(
        input: List<List<String>>,
        rounds: Int,
        newItemLevelModificator: (BigInteger, BigInteger) -> BigInteger
    ): BigInteger {
        val monkeyMap = mutableMapOf<Int, Monkey>()
        input.mapIndexed { monkeyIndex, unparsedMonkey ->
            parseMonkey(unparsedMonkey, monkeyMap, monkeyIndex)
        }

        val inspectMap = mutableMapOf<Int, BigInteger>()
        val divisor = monkeyMap.values.map { it.divisible }.reduce(Int::times).toBigInteger()

        for (round in 0 until rounds) {
            monkeyMap.forEach { (index, monkey) ->
                while (monkey.items.isNotEmpty()) {
                    val item = monkey.items.removeFirst()
                    inspectMap.merge(index, BigInteger.ONE, BigInteger::plus)

                    val newItemLvl = newItemLevelModificator.invoke(getNewItemLvl(item, monkey.operation), divisor)

                    if (newItemLvl.mod(monkey.divisible.toBigInteger()).compareTo(BigInteger.ZERO) == 0) {
                        monkeyMap[monkey.testTrueMonkeyNumber]?.items?.addLast(newItemLvl)
                    } else {
                        monkeyMap[monkey.testFalseMonkeyNumber]?.items?.addLast(newItemLvl)
                    }
                }
            }
        }

        return inspectMap.values.sortedDescending().take(2).reduce(BigInteger::times)
    }

    fun part1(input: List<List<String>>): BigInteger {
        return catchMonkeysItems(input, 20) { itemLvl, _ -> itemLvl.divide(BigInteger("3")) }
    }

    fun part2(input: List<List<String>>): BigInteger {
        return catchMonkeysItems(input, 10000) { itemLvl, divisor -> itemLvl.remainder(divisor) }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readGroupedInput("Day11_test")
    println(part1(testInput))
    println(part2(testInput))

    val input = readGroupedInput("Day11")
    println(part1(input))
    println(part2(input))
}

data class Monkey(
    val items: ArrayDeque<BigInteger>,
    val operation: String,
    val divisible: Int,
    val testTrueMonkeyNumber: Int,
    val testFalseMonkeyNumber: Int
)