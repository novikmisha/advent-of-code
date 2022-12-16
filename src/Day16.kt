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
        results: MutableSet<Pair<Int, Set<String>>>,
        toVisit: MutableSet<String>
    ) {
        if (stepsLeft < 1) {
            results.add(Pair(flowRate, toVisit))
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

        toVisit.remove(node.name)
        if (toVisit.isEmpty()) {
            results.add(Pair(currentFlowRate, toVisit))
            return
        }

        if (currentSteps < 1) {
            results.add(Pair(currentFlowRate, toVisit))
            return
        }

        val distanceMap = findDistances(valveMap, currentNode, toVisit)
        for (nextNode in toVisit) {

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
                toVisit.toMutableSet()
            )
        }

    }


    fun part1(input: List<String>): Int {
        val valveMap = buildValveMap(input)

        val results = mutableSetOf<Pair<Int, Set<String>>>()

        val toVisit = valveMap.filter { it.value.flowRate > 0 }.keys.toMutableSet()
        dfs(valveMap, 30, "AA", 0, results, toVisit)
        return results.maxBy { it.first }.first
    }

    fun part2(input: List<String>): Int {
        val valveMap = buildValveMap(input)

        var results = mutableSetOf<Pair<Int, Set<String>>>()

        val toVisit = valveMap.filter { it.value.flowRate > 0 }.keys.toMutableSet()
        dfs(valveMap, 26, "AA", 0, results, toVisit)

        val resultList = results.toList()
        var maxSize = 0
        for (i in 0 until resultList.size - 1) {
            for (j in i + 1 until resultList.size) {
                val sum = resultList[i].first + resultList[j].first
                if (sum > maxSize) {
                    val visitedHuman = toVisit.toMutableSet()
                    visitedHuman.removeAll(resultList[i].second)

                    val visitedElephans = toVisit.toMutableSet()
                    visitedElephans.removeAll(resultList[j].second)

                    if (visitedHuman.intersect(visitedElephans).isEmpty()) {
                        maxSize = sum
                    }
                }
            }
        }
        return maxSize
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

