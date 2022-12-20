import java.util.*

fun main() {
    fun part1(input: List<String>): Long {
        val intInput = input.map { it.toLong() }
            .mapIndexed { index, value ->
                SortNode(index, value)
            }

        val arrayToSort = LinkedList<SortNode>()
        arrayToSort.addAll(intInput)
        for (i in intInput) {
            val indexOf = arrayToSort.indexOf(i)
            val newPosition = (indexOf + i.value).mod(arrayToSort.size - 1)
            arrayToSort.removeAt(indexOf)
            if (newPosition == 0) {
                arrayToSort.add(i)
            } else {
                arrayToSort.add(newPosition, i)
            }
        }
        val start = arrayToSort.indexOfFirst { it.value == 0L }
        val first = (1000 + start).mod(arrayToSort.size)
        val second = (2000 + start).mod(arrayToSort.size)
        val third = (3000 + start).mod(arrayToSort.size)
        return arrayToSort[first].value + arrayToSort[second].value + arrayToSort[third].value
    }

    fun part2(input: List<String>): Long {
        val intInput = input.map { it.toLong() * 811589153 }
            .mapIndexed { index, value ->
                SortNode(index, value)
            }

        val arrayToSort = LinkedList<SortNode>()
        arrayToSort.addAll(intInput)
        repeat(10) {
            for (i in intInput) {
                val indexOf = arrayToSort.indexOf(i)
                val newPosition = (indexOf + i.value).mod(arrayToSort.size - 1)
                arrayToSort.removeAt(indexOf)
                if (newPosition == 0) {
                    arrayToSort.add(i)
                } else {
                    arrayToSort.add(newPosition, i)
                }
            }
        }
        val start = arrayToSort.indexOfFirst { it.value == 0L }
        val first = (1000 + start).mod(arrayToSort.size)
        val second = (2000 + start).mod(arrayToSort.size)
        val third = (3000 + start).mod(arrayToSort.size)
        return arrayToSort[first].value + arrayToSort[second].value + arrayToSort[third].value
    }

//     test if implementation meets criteria from the description, like:
    val testInput = readInput("Day20_test")
    println(part1(testInput))
    println(part2(testInput))

    val input = readInput("Day20")
    println(part1(input))
    println(part2(input))
}

data class SortNode(val id: Int, val value: Long)