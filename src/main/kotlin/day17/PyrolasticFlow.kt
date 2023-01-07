package day17

import util.readFileLines

typealias Rock = List<Pair<Long, Long>>

/**
 * ####
 *
 * .#.
 * ###
 * .#.
 *
 * ..#
 * ..#
 * ###
 *
 * #
 * #
 * #
 * #
 *
 * ##
 * ##
 */
private val rockShapes: List<Rock> = listOf(
    listOf(0L to 0L, 1L to 0L, 2L to 0L, 3L to 0L),
    listOf(1L to 0, 0L to 1L, 1L to 1L, 2L to 1L, 1L to 2L),
    listOf(0L to 0, 1L to 0L, 2L to 0L, 2L to 1L, 2L to 2L),
    listOf(0L to 0, 0L to 1L, 0L to 2L, 0L to 3L),
    listOf(0L to 0, 1L to 0L, 0L to 1L, 1L to 1L)
)

fun String.parseInput(): List<Direction> = map {
    when (it) {
        '>' -> Direction.Right
        '<' -> Direction.Left
        else -> error("Unknown input")
    }
}

fun solveDay171() {
    val input = readFileLines("/day17.txt").first().parseInput()

    val result = simulateRockFall(input)
    println(result)
}

fun simulateRockFall(
    shifts: List<Direction>,
    rocks: List<Rock> = rockShapes,
    fallenRocksLimit: Long = 2022,
    chamberWidth: Int = 7
): Long {
    val chamberStart = 0
    val chamberEnd = chamberWidth - 1

    var stoppedRocks = mutableListOf<Rock>()

    var nextRockIndex = 0
    var nextShiftIndex = 0

    var rock = rocks[0].shift(2, 4)

    fun printState() {
        val maxHeight = (stoppedRocks.flatten() + rock).maxOf { it.second }
        println()

        for (height in maxHeight downTo 1L) {
            print("|")
            for (width in 0L until chamberWidth) {
                val cords = width to height
                print(
                    when {
                        cords in rock -> '@'
                        stoppedRocks.any { cords in it } -> '#'
                        else -> "."
                    }
                )
            }
            print("|")
            println()
        }
        println("+-------+")
    }

    while (stoppedRocks.size < fallenRocksLimit) {
        val shiftDirection = shifts[nextShiftIndex]
        nextShiftIndex = (nextShiftIndex + 1) % shifts.size

        val shifted = rock.shift(x = shiftDirection.shift, y = 0)
        if (shifted.left >= chamberStart && shifted.right <= chamberEnd && shifted.none { it in stoppedRocks.flatten() }) {
            rock = shifted
        }

        val movedDown = rock.shift(x = 0, y = -1)

        val hitGround = movedDown.minOf { it.second } < 1
        val hitOtherRock = stoppedRocks.any { stoppedRock ->
            stoppedRock.any { position -> position in movedDown }
        }

        if (hitGround || hitOtherRock) {
            nextRockIndex = (nextRockIndex + 1) % rocks.size
            stoppedRocks.add(rock)

            val height = stoppedRocks.flatten().maxOf { it.second }

            rock = rocks[nextRockIndex].shift(2, height + 4)
        } else {
            rock = movedDown
        }
    }

    return stoppedRocks.flatten().maxOf { it.second }
}

val Rock.left: Long
    get() = minOf { it.first }

val Rock.right: Long
    get() = maxOf { it.first }

fun Rock.shift(x: Long, y: Long): Rock = map {
    it.first + x to it.second + y
}

enum class Direction(val shift: Long) {
    Left(-1), Right(1)
}

fun main() {
    solveDay171()
}