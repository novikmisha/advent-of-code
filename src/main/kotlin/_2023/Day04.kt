package _2023

import Day
import InputReader
import inc
import kotlin.math.pow

class Day04 : Day(2023, 4) {

    override val firstTestAnswer = 13
    override val secondTestAnswer = 30

    override fun first(input: InputReader) = input.asLines()
        .sumOf { card ->
            val (winningNumbers, cardNumbers) = card.dropWhile { it != ':' }.drop(1).split("|").map {
                it.trim().split(" ").mapNotNull { number -> number.trim().toIntOrNull() }.toSet()
            }

            val wonNumbers = winningNumbers.intersect(cardNumbers).size

            if (wonNumbers != 0) (2.0).pow(wonNumbers - 1).toInt() else 0
        }

    override fun second(input: InputReader): Int {
        val cards = input.asLines()
        val cardAmount = cards.associateTo(mutableMapOf()) { it.drop(5).takeWhile { it != ':' }.trim().toInt() to 1 }

        cards.forEach { card ->
            val cardNumber = card.drop(5).takeWhile { it != ':' }.trim().toInt()
            val (winningNumbers, cardNumbers) = card.dropWhile { it != ':' }.drop(1).split("|").map {
                it.trim().split(" ").mapNotNull { number -> number.trim().toIntOrNull() }.toSet()
            }

            val wonNumbers = winningNumbers.intersect(cardNumbers).size

            for (wonCard in cardNumber + 1..cardNumber + wonNumbers) {
                cardAmount.inc(wonCard, cardAmount[cardNumber]!!)
            }
        }

        return cardAmount.values.sum()
    }
}


fun main() {
    Day04().solve()
}