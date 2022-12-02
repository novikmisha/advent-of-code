// seen more elegant solutions via myFigure - "X" - maps into number
// and you win when your figure = opponent's figure +1 (mod 3 ofc)
fun main() {

    val scoreOfFigureMap = mapOf("X" to 1, "Y" to 2, "Z" to 3)
    val enemyWinMap = mapOf("A" to "Z", "B" to "X", "C" to "Y")
    val drawMap = mapOf("A" to "X", "B" to "Y", "C" to "Z")

    fun scoreOfMyShape(figure: String): Int {
        return scoreOfFigureMap.getOrDefault(figure, 0)
    }

    fun gameScore(enemyFigure: String, myFigure: String): Int {
        if (drawMap.getOrDefault(enemyFigure, "") == myFigure) {
            return 3
        }
        if (enemyWinMap.getOrDefault(enemyFigure, "") != myFigure) {
            return 6
        }
        return 0
    }

    fun chooseFigure(enemyFigure: String, gameResult: String): String {
        if ("X" == gameResult) {
            // lose
            return enemyWinMap.getOrDefault(enemyFigure, "")
        } else if ("Y" == gameResult) {
            // draw
            return drawMap.getOrDefault(enemyFigure, "")
        } else if ("Z" == gameResult) {
            // win
            var figure = "XYZ"
            // not draw
            figure = figure.replace(drawMap.getOrDefault(enemyFigure, ""), "")
            // not lose
            figure = figure.replace(enemyWinMap.getOrDefault(enemyFigure, ""), "")
            return figure
        }

        return ""
    }

    fun part1(input: List<String>): Int {
        return input.sumOf {

            val split = it.split(' ')

            var enemy = split[0]
            var me = split[1]

            var score = 0
            score += scoreOfMyShape(me)
            score += gameScore(enemy, me)

            score
        }
    }

    fun part2(input: List<String>): Int {
        return input.sumOf {

            val split = it.split(' ')

            var enemyFigure = split[0]
            var gameResult = split[1]

            var myFigure = chooseFigure(enemyFigure, gameResult)
            var score = 0
            score += scoreOfMyShape(myFigure)
            score += gameScore(enemyFigure, myFigure)

            score
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    println(part1(testInput))
    println(part2(testInput))

    val input = readInput("Day02")
    println(part1(input))
    println(part2(input))
}
