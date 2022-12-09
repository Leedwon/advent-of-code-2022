package day7

import util.readFileLines

private const val fileName = "/day7.txt"

fun solveDay71() {
    val input = readFileLines(fileName)

    val root = buildFileSystem(input)
    val sum = root.totalSizeOfDirectoriesNoGreaterThan(100_000)
    println(sum)
}

fun solveDay72() {
    val input = readFileLines(fileName)

    val maxSpace = 70_000_000
    val requiredSpace = 30_000_000

    val root = buildFileSystem(input)

    val dirSizes = root.calculateDirSizes()

    val freeSpace = maxSpace - dirSizes[root]!!

    val minValue = dirSizes
        .filter { freeSpace + it.value >= requiredSpace }
        .minOf { it.value }

    println(minValue)
}

fun Node.Directory.calculateDirSizes(): Map<Node, Int> {
    val sizes = mutableMapOf<Node, Int>()

    val root = this
    root.postorder { node ->
        when (node) {
            is Node.Directory -> {
                sizes[node] = node.children.sumOf { sizes[it]!! }
            }
            is Node.File -> sizes[node] = node.size
        }
    }
    return sizes
}

fun Node.Directory.totalSizeOfDirectoriesNoGreaterThan(fileSize: Int): Int {
    val sizes = calculateDirSizes()
    return sizes.filter { it.key is Node.Directory }.values.filter { it <= fileSize }.sum()
}

private fun Node.postorder(operation: (Node) -> Unit) {
    children.forEach { child -> child.postorder(operation) }
    operation(this)
}

/**
 * @return root Directory
 */
private fun buildFileSystem(input: List<String>): Node.Directory {
    val stack = ArrayDeque(input)

    val head = stack.removeFirst()
    check(head.isCommand() && head.parseCommand() is Command.ChangeDirectory) { "First input must be a cd command, so we have a reference point" }

    val parentName = (head.parseCommand() as Command.ChangeDirectory).destination

    val root = Node.Directory(name = parentName, parent = null)

    var parent = root
    while (!stack.isEmpty()) {
        val currentLine = stack.removeFirst()
        when (currentLine.isCommand()) {
            true -> {
                when (val command = currentLine.parseCommand()) {
                    is Command.ChangeDirectory -> {
                        parent = if (command.destination == "..") {
                            val oldParent = parent.parent
                            check(oldParent != null) { "Can't move up, already in root directory " }
                            oldParent
                        } else {
                            val newParent =
                                parent.children.firstOrNull { it is Node.Directory && it.name == command.destination }
                            check(newParent != null) { "Can't move to ${command.destination} no such directory" }
                            newParent as Node.Directory
                        }
                    }
                    Command.ListBegin -> {
                        // we can ignore it to simplify things
                    }
                }
            }
            false -> {
                val node = currentLine.parseNode(parent)
                parent.addChild(node)
            }
        }
    }

    return root
}

private fun String.isCommand() = this.startsWith("$")

private fun String.parseCommand(): Command {
    val withoutCommandSign = this.drop(1).trim()
    return when {
        withoutCommandSign.startsWith("cd") -> withoutCommandSign.parseChangeDirectory()
        withoutCommandSign.startsWith("ls") -> Command.ListBegin
        else -> error("Unknown command $this")
    }
}

private fun String.parseNode(parent: Node.Directory): Node {
    return when (isDir()) {
        true -> parseDir(parent)
        false -> parseFile(parent)
    }
}

private fun String.isDir() = startsWith("dir")

private fun String.parseDir(parent: Node.Directory): Node.Directory {
    val name = removePrefix("dir").trim()
    return Node.Directory(name = name, parent = parent)
}

private fun String.parseFile(parent: Node.Directory): Node.File {
    val (sizeString, name) = split(" ")
    return Node.File(
        name = name,
        parent = parent,
        size = sizeString.toInt()
    )
}

private fun String.parseChangeDirectory(): Command.ChangeDirectory {
    return Command.ChangeDirectory(destination = this.removePrefix("cd").trim())
}

private sealed class Command {
    object ListBegin : Command()
    data class ChangeDirectory(val destination: String) : Command()
}

sealed class Node {
    abstract val name: String
    abstract val children: List<Node>
    abstract val parent: Directory?

    data class Directory(override val name: String, override val parent: Directory?) : Node() {

        private val _children: MutableList<Node> = mutableListOf()
        override val children: List<Node> get() = _children

        fun addChild(node: Node) {
            _children.add(node)
        }
    }

    data class File(override val name: String, override val parent: Directory, val size: Int) : Node() {
        override val children: List<Node> = emptyList() // file can't have children
    }
}
