package _2023

import Day
import InputReader

class Day15 : Day(2023, 15) {

    override val firstTestAnswer = 1320L
    override val secondTestAnswer = 145


    override fun first(input: InputReader) = input.asLine().split(",").sumOf {
        it.fold(0L) { result, char ->
            val ascii = char.code
            ((result + ascii) * 17) % 256
        }
    }

    override fun second(input: InputReader): Int {
        val lines = input.asLine().split(",")
        val boxes = mutableMapOf<Int, LinkedHashMap<String, Int>>()
        lines.forEach {
            if (it.contains("=")) {
                val label = it.dropLast(2)

                val hash = label.fold(0) { result, char ->
                    val ascii = char.code
                    ((result + ascii) * 17) % 256
                }

                boxes.getOrPut(hash) { linkedMapOf() }[label] = it.last().digitToInt()
            } else {
                val label = it.dropLast(1)

                val hash = label.fold(0) { result, char ->
                    val ascii = char.code
                    ((result + ascii) * 17) % 256
                }
                boxes.getOrPut(hash) { linkedMapOf() }.remove(label)
            }
        }
        return boxes.entries.filter { it.value.isNotEmpty() }
            .sumOf { (it.key + 1) * it.value.values.mapIndexed { index, lens -> (index + 1) * lens }.sum() }
    }
}

fun main() {
    Day15().solve()
}