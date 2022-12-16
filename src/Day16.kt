fun main() {

    fun findDistances(
        valveMap: Map<String, WaterNode>,
        startNode: String,
        toVisit: Set<String>
    ): Map<String, Int> {
        if (toVisit.isEmpty()) {
            return emptyMap()
        }

        val start = valveMap[startNode]!!
        val queue = mutableListOf(start)
        val distanceMap = mutableMapOf(start.name to 0)

        while (queue.isNotEmpty()) {
            val currentNode = queue.removeFirst()
            val toVisitFromCurrent = currentNode.child
            for (child in toVisitFromCurrent) {

                val currentDistance = distanceMap.getOrDefault(child, Int.MAX_VALUE)
                val newDistance = distanceMap[currentNode.name]!! + 1

                if (newDistance < currentDistance) {
                    distanceMap[child] = newDistance
                    val childNode = valveMap[child]!!
                    queue.remove(childNode)
                    queue.add(childNode)
                    queue.sortBy { distanceMap[it.name]!! }
                }
            }
        }

        return distanceMap
    }

    fun buildValveMap(input: List<String>) = input.associate {
        val (flowString, childStrings) = it.split(";")
        val flowStringSplitted = flowString.split(" ")

        val name = flowStringSplitted[1]
        val flowRate = flowStringSplitted[4].replace("rate=", "").toInt()

        val child = childStrings.replace("tunnels lead to valves", "")
            .replace("tunnel leads to valve", "")
            .split(",")
            .map { str ->
                str.trim()
            }


        name to WaterNode(name, flowRate, false, child)
    }

    fun dfs(
        valveMap: Map<String, WaterNode>, stepsLeft: Int,
        currentNode: String,
        flowRate: Int,
        results: MutableSet<Int>,
        toVisit: Set<String>
    ) {
        if (stepsLeft < 1) {
            results.add(flowRate)
            return
        }

        var currentSteps = stepsLeft
        var currentFlowRate = flowRate
        val node = valveMap[currentNode]!!

        if (node.flowRate > 0) {
            currentSteps -= 1
            val releasedPressure = (node.flowRate * currentSteps)
            currentFlowRate += releasedPressure
        }

        if (currentSteps < 1) {
            results.add(currentFlowRate)
            return
        }

        val newToVisit = toVisit.toMutableSet()
        newToVisit.remove(node.name)
        if (newToVisit.isEmpty()) {
            results.add(currentFlowRate)
            return
        }

        val distanceMap = findDistances(valveMap, currentNode, newToVisit)
        for (nextNode in newToVisit) {

            val distance = distanceMap[nextNode]!!
            if (distance >= currentSteps + 1) {
                continue
            }

            dfs(
                valveMap,
                currentSteps - distance,
                nextNode,
                currentFlowRate,
                results,
                newToVisit,
            )
        }
    }


    fun part1(input: List<String>): Int {
        val valveMap = buildValveMap(input)

        val results = mutableSetOf<Int>()

        val toVisit = valveMap.filter { it.value.flowRate > 0 }.keys
        dfs(valveMap, 30, "AA", 0, results, toVisit)
        return results.max()
    }

    fun part2(input: List<String>): Int {
        return 0
    }


//     test if implementation meets criteria from the description, like:
    val testInput = readInput("Day16_test")
    println(part1(testInput))
    println(part2(testInput))

    val input = readInput("Day16")
    println(part1(input))
    println(part2(input))
}

data class WaterNode(
    var name: String, var flowRate: Int, var isOpened: Boolean, val child: List<String>,
)

