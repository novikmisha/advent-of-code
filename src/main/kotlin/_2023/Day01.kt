package _2023

import Day
import InputReader

class Day01: Day(2023, 1) {

    override val firstTestAnswer = 1
    override val secondTestAnswer = 2

    override fun first(input: InputReader): Any {
        println(input.asLine())
        return 1
    }

    override fun second(input: InputReader): Any {
        println(input.asLine())
        return 2
    }
}

fun main() {
    Day01().solve()
}