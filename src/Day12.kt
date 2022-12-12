fun main() {
    fun getChar(input: List<String>, vertex: Pair<Int, Int>): Char {
        var first = input[vertex.first][vertex.second]
        if (first == 'S') {
            first = 'a'
        } else if (first == 'E') {
            first = 'z'
        }
        return first
    }

    fun isValidVertex(currentVertex: Pair<Int, Int>, vertex: Pair<Int, Int>, input: List<String>): Boolean {
        val maxRow = input.size
        val maxColumn = input.first().length
        if (vertex.first in (0 until maxRow)
            && vertex.second in (0 until maxColumn)
        ) {

            val first = getChar(input, currentVertex)
            val second = getChar(input, vertex)

            return first - second >= -1
        }
        return false
    }

    fun part1(input: List<String>): Int {
        var start = Pair(0, 0)
        var end = Pair(0, 0)

        input.forEachIndexed { rowIndex, row ->
            row.forEachIndexed { columnIndex, char ->
                if (char == 'E') {
                    end = Pair(rowIndex, columnIndex)
                } else if (char == 'S') {
                    start = Pair(rowIndex, columnIndex)
                }
            }
        }

        val queue = mutableListOf(start)
        val distanceMap = mutableMapOf(start to 0)
        while (queue.isNotEmpty()) {
            val currentVertex = queue.removeFirst()

            val vertexesToVisit = listOf(
                Pair(currentVertex.first + 1, currentVertex.second),
                Pair(currentVertex.first - 1, currentVertex.second),
                Pair(currentVertex.first, currentVertex.second + 1),
                Pair(currentVertex.first, currentVertex.second - 1),
            )

            val validVertexes = vertexesToVisit.filter { isValidVertex(currentVertex, it, input) }
            for (vertex in validVertexes) {
                val currentDistance = distanceMap.getOrDefault(vertex, Int.MAX_VALUE)
                val newDistance = distanceMap[currentVertex]!! + 1

                if (newDistance < currentDistance) {
                    distanceMap[vertex] = newDistance
                    queue.remove(vertex)
                    queue.add(vertex)
                    queue.sortBy { distanceMap[it]!! }
                }
            }
        }

        return distanceMap.getOrDefault(end, Int.MAX_VALUE)
    }

    fun part2(input: List<String>): Int {
        val starts = mutableListOf<Pair<Int, Int>>()
        var end = Pair(0, 0)

        input.forEachIndexed { rowIndex, row ->
            row.forEachIndexed { columnIndex, char ->
                if (char == 'E') {
                    end = Pair(rowIndex, columnIndex)
                } else if (char == 'S' || char == 'a') {
                    starts.add(Pair(rowIndex, columnIndex))
                }
            }
        }

        val distances = mutableListOf<Int>()
        for (start in starts) {
            val queue = mutableListOf(start)
            val distanceMap = mutableMapOf(start to 0)
            while (queue.isNotEmpty()) {
                val currentVertex = queue.removeFirst()

                val vertexesToVisit = listOf(
                    Pair(currentVertex.first + 1, currentVertex.second),
                    Pair(currentVertex.first - 1, currentVertex.second),
                    Pair(currentVertex.first, currentVertex.second + 1),
                    Pair(currentVertex.first, currentVertex.second - 1),
                )

                val validVertexes = vertexesToVisit.filter { isValidVertex(currentVertex, it, input) }
                for (vertex in validVertexes) {
                    val currentDistance = distanceMap.getOrDefault(vertex, Int.MAX_VALUE)
                    val newDistance = distanceMap[currentVertex]!! + 1

                    if (newDistance < currentDistance) {
                        distanceMap[vertex] = newDistance
                        queue.remove(vertex)
                        queue.add(vertex)
                        queue.sortBy { distanceMap[it]!! }
                    }
                }
            }

            distances.add(distanceMap.getOrDefault(end, Int.MAX_VALUE))
        }

        return distances.min()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day12_test")
    println(part1(testInput))
    println(part2(testInput))

    val input = readInput("Day12")
    println(part1(input))
    println(part2(input))
}