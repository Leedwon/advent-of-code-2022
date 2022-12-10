package day9

import util.readFileLines

private const val fileName = "/day9.txt"

fun solveDay91() {
    val lines = readFileLines(fileName)

    val commands = lines.map { it.parseCommand() }
    val rope = Rope(Position(0, 0), 2)
    val result = rope.calculateUniqueTailPlaces(commands)
    println(result)
}

fun solveDay92() {
    val lines = readFileLines(fileName)

    val commands = lines.map { it.parseCommand() }
    val rope = Rope(Position(0, 0), 10)
    val result = rope.calculateUniqueTailPlaces(commands)
    println(result)
}

fun Rope.calculateUniqueTailPlaces(moveCommand: List<MoveCommand>): Int {
    val placesVisited = mutableSetOf(this.tail)
    moveCommand.forEach { moveCommand ->
        repeat(moveCommand.times) {
            move(moveCommand.direction)
            placesVisited.add(tail)
        }
    }

    return placesVisited.size
}

class Rope(initialPosition: Position, size: Int) {

    private val rope = MutableList(size) { initialPosition }

    val head
        get() = rope.first()

    val tail
        get() = rope.last()

    private fun Position.shouldMove(parent: Position): Boolean {
        val distanceVector = parent - this
        return distanceVector.x !in (-1..1) || distanceVector.y !in (-1..1)
    }

    fun move(direction: Direction) {
        rope.forEachIndexed { index, position ->
            val moveVector = if (index == 0) {
                direction.moveVector()
            } else {
                val parent = rope[index - 1]
                if (position.shouldMove(parent)) {
                    val distanceVector = parent - position
                    childMoveVector(distanceVector)
                } else {
                    Position(0, 0)
                }
            }
            rope[index] += moveVector
        }
    }

    private fun childMoveVector(distanceVector: Position): Position {
        return when {
            distanceVector.x > 1 && distanceVector.y == 0 -> Position(1, 0)
            distanceVector.x < -1 && distanceVector.y == 0 -> Position(-1, 0)
            distanceVector.x == 0 && distanceVector.y > 1 -> Position(0, 1)
            distanceVector.x == 0 && distanceVector.y < -1 -> Position(0, -1)

            distanceVector.x > 1 && distanceVector.y > 0 ||
                    distanceVector.x > 0 && distanceVector.y > 1 -> Position(1, 1) // bottom-right

            distanceVector.x > 1 && distanceVector.y < 0 ||
                    distanceVector.x > 0 && distanceVector.y < -1 -> Position(1, -1) // top-right

            distanceVector.x < -1 && distanceVector.y > 0 ||
                    distanceVector.x < 0 && distanceVector.y > 1 -> Position(-1, 1) // bottom-left

            distanceVector.x < -1 && distanceVector.y < 0 ||
                    distanceVector.x < 0 && distanceVector.y < -1 -> Position(-1, -1) // top-left

            else -> error("Child shouldn't move")
        }
    }
}

private fun String.parseCommand(): MoveCommand {
    val (dirString, times) = this.split(" ")
    val direction = when (dirString) {
        "R" -> Direction.Right
        "L" -> Direction.Left
        "U" -> Direction.Up
        "D" -> Direction.Down
        else -> error("Wrong direction input")
    }
    return MoveCommand(
        direction = direction,
        times = times.toInt()
    )
}

data class MoveCommand(
    val direction: Direction,
    val times: Int
)

private fun Direction.moveVector(): Position = when (this) {
    Direction.Up -> Position(0, -1)
    Direction.Down -> Position(0, 1)
    Direction.Left -> Position(-1, 0)
    Direction.Right -> Position(1, 0)
}

enum class Direction {
    Up,
    Down,
    Left,
    Right
}

data class Position(val x: Int, val y: Int) {
    operator fun plus(other: Position): Position {
        return Position(x = x + other.x, y = y + other.y)
    }

    operator fun minus(other: Position): Position {
        return Position(x = x - other.x, y = y - other.y)
    }
}
