package day5

import util.readFileLines

private const val fileName = "/day5.txt"

// input characteristics for easier file parsing, it could be done better, but it's fine for a Christmas puzzle :D
private const val stackInputLines = 8
private const val skipLines = 2
private const val stacksCount = 9

fun solveDay51() {
    val lines = readFileLines(fileName)
    val stackInput = lines.take(stackInputLines)
    val movesInput = lines.drop(stackInputLines + skipLines)

    val stacks = parseStacks(stackInput)
    val moves = parseMoves(movesInput)

    stacks.rearrangeStacks(moves = moves, retainOrder = false)
    stacks.forEach {
        print(it.first())
    }
    println()
}

fun solveDay52() {
    val lines = readFileLines(fileName)
    val stackInput = lines.take(stackInputLines)
    val movesInput = lines.drop(stackInputLines + skipLines)

    val stacks = parseStacks(stackInput)
    val moves = parseMoves(movesInput)

    stacks.rearrangeStacks(moves = moves, retainOrder = true)
    stacks.forEach {
        print(it.first())
    }
    println()
}

fun <T> List<ArrayDeque<T>>.rearrangeStacks(moves: List<Move>, retainOrder: Boolean) {
    moves.forEach { move ->
        val fromStack = this[move.fromStack - 1]
        val toStack = this[move.toStack - 1]

        val elementsToMove = mutableListOf<T>()

        repeat(move.howMany) {
            elementsToMove.add(fromStack.removeFirst())
        }

        if (retainOrder) elementsToMove.reverse()

        elementsToMove.forEach {
            toStack.addFirst(it)
        }
    }
}

fun parseStacks(stackInput: List<String>): List<ArrayDeque<String>> {
    return buildList {
        stackInput
            .map { line -> line.chunked(4).map { stackEntry -> stackEntry.trim() } }
            .forEach { stackLine ->
                stackLine.forEachIndexed { index, stackElement ->
                    if (getOrNull(index) == null) {
                        add(index, ArrayDeque())
                    }

                    val runningStack = get(index)
                    if (stackElement.isNotEmpty()) {
                        runningStack.addLast(stackElement[1].toString())
                    }
                }
            }
    }
}

fun parseMoves(movesInput: List<String>): List<Move> {
    return movesInput.map { it.parseMove() }
}

/**
 * Parses Strings in format: "move x from y to z"
 * @return Move with:
 * [Move.howMany] = x
 * [Move.fromStack] = y
 * [Move.toStack] = z
 */
private fun String.parseMove(): Move {
    val numbers = mutableListOf<Int>()
    var runningNumber: Int? = null
    forEach {
        if (it.isDigit()) {
            val current = runningNumber
            runningNumber = if (current == null) {
                it.toString().toInt()
            } else {
                current * 10 + it.toString().toInt()
            }
        } else {
            runningNumber?.let { newNumber ->
                numbers.add(newNumber)
                runningNumber = null
            }
        }
    }
    runningNumber?.let { numbers.add(it) }

    check(numbers.size == 3) { "Invalid move format" }
    return Move(
        howMany = numbers[0],
        fromStack = numbers[1],
        toStack = numbers[2]
    )
}

data class Move(
    val howMany: Int,
    val fromStack: Int,
    val toStack: Int
)
