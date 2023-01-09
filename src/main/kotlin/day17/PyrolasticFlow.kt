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

    val ground = mutableMapOf(
        0L to 0L,
        1L to 0L,
        2L to 0L,
        3L to 0L,
        4L to 0L,
        5L to 0L,
        6L to 0L
    )

    var nextRockIndex = 0
    var nextShiftIndex = 0

    var cycleFound = false
    var moves = 0

    var rock = rocks[0].shift(2, 4)

    val rocksThatHitGround = hashMapOf<>()

    while (!cycleFound) {
        val shiftDirection = shifts[nextShiftIndex]
        nextShiftIndex = (nextShiftIndex + 1) % shifts.size

        val shifted = rock.shift(x = shiftDirection.shift, y = 0)

        if (shifted.left >= chamberStart && shifted.right <= chamberEnd && shifted.none { it.second <= ground[it.first]!! }) {
            rock = shifted
        }

        val movedDown = rock.shift(x = 0, y = -1)

        val hitGround = movedDown.any { it.second <= ground[it.first]!! }

        if (hitGround) {
            nextRockIndex = (nextRockIndex + 1) % rocks.size

            rock.forEach { ground[it.first] = maxOf(ground[it.first]!!, it.second) }

            val height = ground.values.maxOf { it }

            rock = rocks[nextRockIndex].shift(2, height + 4)
        } else {
            rock = movedDown
        }
        moves++
    }

    val height = ground.values.maxOf { it } // height that is added per cycle
    return height
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