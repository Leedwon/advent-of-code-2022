package day14

import util.readFileLines
import kotlin.math.abs

private const val fileName = "/day14.txt"

fun solveDay141() {
    val lines = readFileLines(fileName)

    val cave = initializeCave(lines)
    val result = cave.countSand()
    println(result)
}

fun solveDay142() {
    val lines = readFileLines(fileName)

    val cave = initializeCave(lines, floorHeight = 2)
    val result = cave.countSand(hasFloor = true) + 1 // +1 for entrance
    println(result)
}

fun initializeCave(input: List<String>, entrancePosition: Position = Position(500, 0), floorHeight: Int? = null): Cave {
    val rocks = input.flatMap { it.parseRockPath() }

    val xStart = minOf(entrancePosition.x, rocks.minByOrNull { it.x }!!.x)
    val xEnd = maxOf(entrancePosition.x, rocks.maxByOrNull { it.x }!!.x)
    val yStart = minOf(entrancePosition.y, rocks.minByOrNull { it.y }!!.y)
    val yEnd = maxOf(entrancePosition.y, rocks.maxByOrNull { it.y }!!.y) + (floorHeight ?: 0)

    val columns = xEnd - xStart + 1
    val rows = yEnd - yStart + 1

    val map = mutableMapOf<Position, Type>()

    repeat(columns) { column ->
        val column = xStart + column
        repeat(rows) { row ->
            val row = yStart + row
            val position = Position(x = column, y = row)
            val type = when {
                floorHeight != null && position.y == yEnd -> Type.Rock
                position in rocks -> Type.Rock
                position == entrancePosition -> Type.Entrance
                else -> Type.Air
            }
            map[position] = type
        }
    }

    return Cave(initialPositions = map)
}

fun String.parseRockPath(): List<Position> {
    val positions = this
        .split("->")
        .map {
            val pair = it.trim().split(",")
            pair[0].toInt() to pair[1].toInt()
        }

    return positions.fold(emptyList()) { acc, pair ->
        if (acc.isEmpty()) return@fold listOf(
            Position(
                x = pair.first,
                y = pair.second,
            )
        )
        val lastPosition = acc.last()

        val xDiff = abs(lastPosition.x - pair.first)
        val yDiff = abs(lastPosition.y - pair.second)

        val running = acc.toMutableList()

        if (xDiff > 0) {
            repeat(xDiff) {
                if (lastPosition.x > pair.first) {
                    running.add(
                        Position(
                            x = lastPosition.x - (xDiff - (xDiff - it) + 1),
                            y = lastPosition.y,
                        )
                    )
                } else {
                    running.add(
                        Position(
                            x = lastPosition.x + it + 1,
                            y = lastPosition.y,
                        )
                    )
                }
            }
        }

        if (yDiff > 0) {
            repeat(yDiff) {
                if (lastPosition.y > pair.second) {
                    running.add(
                        Position(
                            x = lastPosition.x,
                            y = lastPosition.y - (yDiff - (yDiff - it) + 1),
                        )
                    )
                } else {
                    running.add(
                        Position(
                            x = lastPosition.x,
                            y = lastPosition.y + it + 1,
                        )
                    )
                }
            }
        }

        running
    }
}

data class Cave(
    val initialPositions: Map<Position, Type>
) {
    private val positions = initialPositions.toMutableMap()

    private val xStart = initialPositions.keys.minByOrNull { it.x }!!.x
    private val xEnd = initialPositions.keys.maxByOrNull { it.x }!!.x
    private val yStart = initialPositions.keys.minByOrNull { it.y }!!.y
    private val yEnd = initialPositions.keys.maxByOrNull { it.y }!!.y

    private val start = initialPositions.filterKeys { positions[it] == Type.Entrance }.keys.first()

    private fun Position.willFallIntoAbyss(): Boolean {
        return x - 1 < xStart || x + 1 > xEnd || y - 1 < yStart || y + 1 > yEnd
    }

    /**
     * @return position to which sand falls or null if it can't move anymore
     */
    private fun Position.sandNextPositionOrNull(hasFloor: Boolean): Position? {
        val down = Position(x, y + 1)
        val leftDown = Position(x - 1, y + 1)
        val rightDown = Position(x + 1, y + 1)
        return when {
            positions[down] == Type.Air || (positions[down] == null && hasFloor && (x - 1 < xStart || x + 1 > xEnd) && y + 1 < yEnd) -> down
            positions[leftDown] == Type.Air || (positions[leftDown] == null && hasFloor && (x - 1 < xStart || x + 1 > xEnd) && y + 1 < yEnd) -> leftDown
            positions[rightDown] == Type.Air || (positions[rightDown] == null && hasFloor && (x - 1 < xStart || x + 1 > xEnd) && y + 1 < yEnd) -> rightDown
            else -> null
        }
    }

    /**
     * @return true when sand landed somewhere false if felt into abyss or blocked the entrance, so there will be no longer place for sand to fall into.
     */
    private fun dropSand(hasFloor: Boolean = false): Boolean {
        var sandPosition = start
        var next = sandPosition.sandNextPositionOrNull(hasFloor)
        while (next != null) {
            sandPosition = next

            if (!hasFloor && sandPosition.willFallIntoAbyss()) return false
            next = sandPosition.sandNextPositionOrNull(hasFloor)
        }
        positions[sandPosition] = Type.Sand
        return sandPosition != start
    }

    fun countSand(hasFloor: Boolean = false): Int {
        var sandUnits = 0
        while (dropSand(hasFloor)) {
            sandUnits++
        }
        return sandUnits
    }

    fun print() {
        val xStart = positions.keys.minByOrNull { it.x }!!.x
        val xEnd = positions.keys.maxByOrNull { it.x }!!.x
        val yStart = positions.keys.minByOrNull { it.y }!!.y
        val yEnd = positions.keys.maxByOrNull { it.y }!!.y

        IntRange(yStart, yEnd).forEach { y ->
            IntRange(xStart, xEnd).forEach { x ->
                print(
                    when (positions[Position(x, y)]) {
                        Type.Rock -> "#"
                        Type.Air -> "."
                        Type.Sand -> "o"
                        Type.Entrance -> "+"
                        null -> ","
                    }
                )
            }
            println()
        }
    }
}

enum class Type {
    Rock, Air, Sand, Entrance
}

data class Position(
    val x: Int,
    val y: Int,
)