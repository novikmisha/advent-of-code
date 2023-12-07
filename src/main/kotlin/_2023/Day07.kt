package _2023

import Day
import InputReader

enum class HandPower(val power: Int, val validation: (cardOccurrences: Map<Char, Int>) -> Boolean) {
    FIVE_OF_A_KIND(6, ::fiveOfAKindValidation),
    FOUR_OF_A_KIND(5, ::fourOfAKindValidation),
    FULL_HOUSE(4, ::fullHouseValidation),
    THREE_OF_A_KIND(3, ::threeOfAKindValidation),
    TWO_PAIRS(2, ::twoPairValidation),
    ONE_PAIR(1, ::onePairValidation),
    HIGH_CARD(0, ::highCardValidation)
}

fun getCardOccurrences(hand: String) = hand.groupingBy { it }.eachCount()

fun highCardValidation(cardOccurrences: Map<Char, Int>) =
    cardOccurrences.filter { it.value == 1 }.size == 5

fun onePairValidation(cardOccurrences: Map<Char, Int>): Boolean {
    val hasPair = cardOccurrences.filter { it.value == 2 }.size == 1
    val allDifferent = cardOccurrences.filter { it.value == 1 }.size == 3
    return hasPair && allDifferent
}

fun twoPairValidation(cardOccurrences: Map<Char, Int>): Boolean {
    val hasPair = cardOccurrences.filter { it.value == 2 }.size == 2
    val allDifferent = cardOccurrences.filter { it.value == 1 }.size == 1
    return hasPair && allDifferent
}

fun threeOfAKindValidation(cardOccurrences: Map<Char, Int>): Boolean {
    val hasThree = cardOccurrences.filter { it.value == 3 }.size == 1
    val allDifferent = cardOccurrences.filter { it.value == 1 }.size == 2
    return hasThree && allDifferent
}

fun fullHouseValidation(cardOccurrences: Map<Char, Int>): Boolean {
    val hasThree = cardOccurrences.filter { it.value == 3 }.size == 1
    val hasPair = cardOccurrences.filter { it.value == 2 }.size == 1
    return hasThree && hasPair
}

fun fourOfAKindValidation(cardOccurrences: Map<Char, Int>) =
    cardOccurrences.filter { it.value == 4 }.size == 1

fun fiveOfAKindValidation(cardOccurrences: Map<Char, Int>) =
    cardOccurrences.filter { it.value == 5 }.size == 1

data class Game(
    val hand: String,
    val bid: Int
)

class Day07 : Day(2023, 7) {

    override val firstTestAnswer = 6440
    override val secondTestAnswer = 5905

    override fun first(input: InputReader): Int {
        fun getHandPower(hand: String): HandPower {
            val cardOccurrences = getCardOccurrences(hand)
            return HandPower.entries.firstOrNull { it.validation(cardOccurrences) } ?: error("$hand has no power")
        }

        val handPowersMap = mutableMapOf<String, HandPower>()
        val cards = listOf(
            'A',
            'K',
            'Q',
            'J',
            'T',
            '9',
            '8',
            '7',
            '6',
            '5',
            '4',
            '3',
            '2',
        )

        val games = input.asLines().map { line ->
            val (hand, bid) = line.split(" ")
            Game(hand, bid.toInt())
        }

        games.forEach { game -> handPowersMap[game.hand] = getHandPower(game.hand) }

        val gameComparator = Comparator<Game> { firstGame, secondGame ->
            val firstHandPower = handPowersMap[firstGame.hand]!!
            val secondHandPower = handPowersMap[secondGame.hand]!!

            if (firstHandPower != secondHandPower) {
                firstHandPower.power - secondHandPower.power
            } else {
                for (i in 0 until 5) {
                    val firstGameCharIndex = cards.indexOf(firstGame.hand[i])
                    val secondGameCharIndex = cards.indexOf(secondGame.hand[i])
                    if (firstGameCharIndex != secondGameCharIndex) {
                        return@Comparator secondGameCharIndex - firstGameCharIndex
                    }
                }
                error("can't get here")
            }
        }

        return games.sortedWith(gameComparator).mapIndexed { index, game ->
            (index+1) * game.bid
        }.sum()
    }

    override fun second(input: InputReader): Int {
        val cards = listOf(
            'A',
            'K',
            'Q',
            'T',
            '9',
            '8',
            '7',
            '6',
            '5',
            '4',
            '3',
            '2',
            'J',
        )

        fun getHandPower(hand: String): HandPower {
            return if (hand.any { it == 'J' }) {
                cards.dropLast(1).map { mimic ->
                    val newHand = hand.replace("J", mimic.toString())
                    getHandPower(newHand)
                }.maxBy { it.power }
            } else {
                val cardOccurrences = getCardOccurrences(hand)
                HandPower.entries.firstOrNull { it.validation(cardOccurrences) } ?: error("$hand has no power")
            }
        }

        val handPowersMap = mutableMapOf<String, HandPower>()

        val games = input.asLines().map { line ->
            val (hand, bid) = line.split(" ")
            Game(hand, bid.toInt())
        }

        games.forEach { game -> handPowersMap[game.hand] = getHandPower(game.hand) }

        val gameComparator = Comparator<Game> { firstGame, secondGame ->
            val firstHandPower = handPowersMap[firstGame.hand]!!
            val secondHandPower = handPowersMap[secondGame.hand]!!

            if (firstHandPower != secondHandPower) {
                firstHandPower.power - secondHandPower.power
            } else {
                for (i in 0 until 5) {
                    val firstGameCharIndex = cards.indexOf(firstGame.hand[i])
                    val secondGameCharIndex = cards.indexOf(secondGame.hand[i])
                    if (firstGameCharIndex != secondGameCharIndex) {
                        return@Comparator secondGameCharIndex - firstGameCharIndex
                    }
                }
                error("can't get here")
            }
        }

        return games.sortedWith(gameComparator).mapIndexed { index, game ->
            (index+1) * game.bid
        }.sum()
    }

}

fun main() {
    Day07().solve()
}