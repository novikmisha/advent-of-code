package _2022

import readInput
import kotlin.math.abs

fun main() {
    fun part1(input: List<String>): Int {
        val queue = ArrayDeque<SimpleCommand>()
        input.forEach {
            if ("noop" == it) {
                queue.add(NoopCommand())
            } else if (it.startsWith("addx")) {
                queue.add(AddXCommand(incX = it.split(" ")[1].toInt()))
            }
        }

        val stateMachine = StateMachine(commands = queue)
        return stateMachine.run()
    }

    fun part2(input: List<String>): List<String> {
        val queue = ArrayDeque<SimpleCommand>()
        input.forEach {
            if ("noop" == it) {
                queue.add(NoopCommand())
            } else if (it.startsWith("addx")) {
                queue.add(AddXCommand(incX = it.split(" ")[1].toInt()))
            }
        }

        val stateMachine = StateMachine(commands = queue)
        val str = stateMachine.run2()
        return str.chunked(40)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10_test")
    println(part1(testInput))
    part2(testInput).forEach(::println)

    val input = readInput("Day10")
    println(part1(input))
    part2(input).forEach(::println)
}

data class StateMachine(var x: Int = 1, var commands: ArrayDeque<SimpleCommand>) {

    fun run(): Int {
        var cycle = 0
        var flag = true
        var currentCommand = commands.removeFirst()
        var signalStrength = 0
        while (flag) {
            cycle++
            currentCommand.beforeCycle(this)

            if (cycle == 20 || (cycle - 20) % 40 == 0) {
                signalStrength += cycle * x
            }
            currentCommand.popCycle()
            currentCommand.afterCycle(this)
            if (currentCommand.cycleCount == 0) {
                if (commands.isEmpty()) {
                    flag = false
                } else {
                    currentCommand = commands.removeFirst()
                }
            }
        }

        return signalStrength
    }

    fun run2(): String {
        var cycle = 0
        var flag = true
        var currentCommand = commands.removeFirst()
        var str = ""
        while (flag) {
            cycle++
            currentCommand.beforeCycle(this)

            str += if (abs(cycle % 40 - (x + 1)) < 2) {
                "#"
            } else {
                "."
            }

            currentCommand.popCycle()
            currentCommand.afterCycle(this)
            if (currentCommand.cycleCount == 0) {
                if (commands.isEmpty()) {
                    flag = false
                } else {
                    currentCommand = commands.removeFirst()
                }
            }
        }

        return str
    }
}


interface Command {
    fun beforeCycle(stateMachine: StateMachine)
    fun duringCycle(stateMachine: StateMachine)
    fun afterCycle(stateMachine: StateMachine)
    fun popCycle()
}

abstract class SimpleCommand(open var cycleCount: Int) : Command {
    override fun popCycle() {
        cycleCount--
    }
}

data class NoopCommand(override var cycleCount: Int = 1) : SimpleCommand(cycleCount) {

    override fun beforeCycle(stateMachine: StateMachine) {
    }

    override fun duringCycle(stateMachine: StateMachine) {
    }

    override fun afterCycle(stateMachine: StateMachine) {
    }
}

data class AddXCommand(override var cycleCount: Int = 2, var incX: Int) : SimpleCommand(cycleCount) {
    override fun beforeCycle(stateMachine: StateMachine) {
    }

    override fun duringCycle(stateMachine: StateMachine) {
    }

    override fun afterCycle(stateMachine: StateMachine) {
        if (cycleCount == 0) {
            stateMachine.x += incX
        }
    }
}