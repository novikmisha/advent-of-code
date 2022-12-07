fun main() {
    fun part1(input: List<String>): Int {
        var node = Node(Node.dir, 0, "/", null, mutableListOf())
        val groupedCommands = mutableListOf<List<String>>()
        var currentCommand = mutableListOf<String>()
        for (s in input) {
            if (s.startsWith("$")) {
                if (currentCommand.size > 0) {
                    groupedCommands.add(currentCommand)
                }
                currentCommand = mutableListOf(s)
            } else {
                currentCommand.add(s)
            }
        }
        if (currentCommand.size > 0) {
            groupedCommands.add(currentCommand)
        }
        groupedCommands.forEach {
            val command = it.first()
            if ("\$ ls" == command) {
                node.addNodes(it.drop(1))
            } else if (command.startsWith("\$ cd")) {
                val dir = command.split(" ").last()
                node = node.cd(dir)!!
            }
        }

        var sum = 0
        node = node.cd("/")!!
        val queue = ArrayDeque<Node>()
        queue.add(node)
        do {
            node = queue.removeFirst()
            queue.addAll(node.child.filter { it.type == Node.dir })
            if (node.size < 100000) {
                sum += node.size
            }
        } while (queue.isNotEmpty())

        return sum
    }

    fun part2(input: List<String>): Int {
        var node = Node(Node.dir, 0, "/", null, mutableListOf())
        val groupedCommands = mutableListOf<List<String>>()
        var currentCommand = mutableListOf<String>()
        for (s in input) {
            if (s.startsWith("$")) {
                if (currentCommand.size > 0) {
                    groupedCommands.add(currentCommand)
                }
                currentCommand = mutableListOf(s)
            } else {
                currentCommand.add(s)
            }
        }
        if (currentCommand.size > 0) {
            groupedCommands.add(currentCommand)
        }
        groupedCommands.forEach {
            val command = it.first()
            if ("\$ ls" == command) {
                node.addNodes(it.drop(1))
            } else if (command.startsWith("\$ cd")) {
                val dir = command.split(" ").last()
                node = node.cd(dir)!!
            }
        }

        val totalSize = 70000000
        val updateSize = 30000000

        node = node.cd("/")!!
        val freeSize = totalSize - node.size
        val sizeToDelete = updateSize - freeSize

        val bigSizes = mutableListOf<Int>()
        val queue = ArrayDeque<Node>()
        queue.add(node)
        do {
            node = queue.removeFirst()
            queue.addAll(node.child.filter { it.type == Node.dir })
            if (node.size >= sizeToDelete) {
                bigSizes.add(node.size)

            }

        } while (queue.isNotEmpty())

        return bigSizes.min()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test")
    println(part1(testInput))
    println(part2(testInput))

    val input = readInput("Day07")
    println(part1(input))
    println(part2(input))


}

data class Node(
    val type: String, var size: Int, val name: String, var root: Node?, val parent: Node?, val child: MutableList<Node>
) {

    constructor(
        type: String, size: Int, name: String, parent: Node?, child: MutableList<Node>
    ) : this(
        type, size, name, null, parent, child
    ) {
        if (root == null) {
            root = this
        }
    }

    companion object FileType {
        val file = "file"
        val dir = "dir"
    }

    fun cd(name: String): Node? {
        if (name == "..") {
            return parent
        }
        if (name == "/") {
            return root
        }
        return child.first { it.name == name }
    }

    fun addNodes(lsOutput: List<String>) {
        child.addAll(lsOutput.map {
            var (size, name) = it.split(" ")
            var fileType = file

            if (size == dir) {
                size = "0"
                fileType = dir
            }

            Node(fileType, size.toInt(), name, root, this, mutableListOf())
        })

        addSizes(child.sumOf { it.size })
    }

    private fun addSizes(sumOf: Int) {
        size += sumOf
        parent?.addSizes(sumOf)
    }
}

