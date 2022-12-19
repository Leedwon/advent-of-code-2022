package day14

import util.readFileLines
import kotlin.math.abs

private const val fileName = "/day14.txt"

fun solveDay141() {
    val lines = readFileLines(fileName)

    val cave = initializeCave(lines)
    val result = cave.countSandBeforeItFallsIntoAbyss()
    println(result)
}

fun initializeCave(input: List<String>, entrancePosition: Position = Position(500, 0)): Cave {
    val rocks = input.flatMap { it.parseRockPath() }

    val xStart = minOf(entrancePosition.x, rocks.minByOrNull { it.x }!!.x)
    val xEnd = maxOf(entrancePosition.x, rocks.maxByOrNull { it.x }!!.x)
    val yStart = minOf(entrancePosition.y, rocks.minByOrNull { it.y }!!.y)
    val yEnd = maxOf(entrancePosition.y, rocks.maxByOrNull { it.y }!!.y)

    val columns = xEnd - xStart + 1
    val rows = yEnd - yStart + 1

    val map = mutableMapOf<Position, Type>()

    repeat(columns) { column ->
        val column = xStart + column
        repeat(rows) { row ->
            val row = yStart + row
            val position = Position(x = column, y = row)
            val type = when (position) {
                in rocks -> Type.Rock
                entrancePosition -> Type.Entrance
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
    private fun Position.sandNextPositionOrNull(): Position? {
        val down = Position(x, y + 1)
        val leftDown = Position(x - 1, y + 1)
        val rightDown = Position(x + 1, y + 1)
        return when {
            positions[down] == Type.Air -> down
            positions[leftDown] == Type.Air -> leftDown
            positions[rightDown] == Type.Air -> rightDown
            else -> null
        }
    }

    /**
     * @return true when sand landed somewhere falls if felt into abyss
     */
    private fun dropSand(): Boolean {
        var sandPosition = start
        var next = sandPosition.sandNextPositionOrNull()
        while (next != null) {
            sandPosition = next
            if (sandPosition.willFallIntoAbyss()) return false
            next = sandPosition.sandNextPositionOrNull()
        }
        positions[sandPosition] = Type.Sand
        return true
    }

    fun countSandBeforeItFallsIntoAbyss(): Int {
        var sandUnits = 0
        while (dropSand()) {
            sandUnits++
        }
        return sandUnits
    }
}

enum class Type {
    Rock, Air, Sand, Entrance
}

data class Position(
    val x: Int,
    val y: Int,
)