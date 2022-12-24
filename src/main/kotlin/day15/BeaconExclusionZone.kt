package day15

import util.readFileLines
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

private const val fileName = "/day15.txt"

fun solveDay151() {
    val lines = readFileLines(fileName)


    val y = 2_000_000

    val data = parseData(lines)

    val minX = data.minOf { min(it.sensor.first - it.radius, it.beacon.first) }
    val maxX = data.maxOf { max(it.sensor.first + it.radius, it.beacon.first) }


    val result = (minX..maxX).count {
        val position = it to y

        data.any { data ->
            !position.canBeBeacon(sensor = data.sensor, beacon = data.beacon, closestBeaconRadius = data.radius)
        }
    }

    println(result)
}

fun solveDay152() {
    val lines = readFileLines(fileName)

    val data = parseData(lines)

    val yMax = 4_000_000

    var finalX: Int? = null
    var finalY: Int? = null

    for (y in (0..yMax)) {
        val excludedRanges = data.mapNotNull { it.getExcludedXRangeOrNull(y, coerceIn = 0..yMax) }.sortedBy { it.first }

        val range = possibleRangeOrNull(excludedRanges)
        if (range != null) {
            finalX = range.first
            finalY = y
            break
        }
    }

    val result = finalX!! * 4_000_000L + finalY!!
    println(result)
}

private fun possibleRangeOrNull(excludedRanges: List<IntRange>): IntRange? {
    excludedRanges.reduce { acc, intRange ->
        when {
            intRange.first > acc.last -> return acc.last + 1 until intRange.first
            intRange.first <= acc.last && intRange.last >= acc.last -> acc.first..intRange.last
            else -> acc
        }
    }
    return null
}

private fun parseData(lines: List<String>): List<Data> {
    val xRegex = Regex("x=.\\d*")
    val yRegex = Regex("y=.\\d*")

    return lines
        .map {
            val xMatches = xRegex.findAll(it).toList()
            val yMatches = yRegex.findAll(it).toList()

            val sensor = xMatches[0].value.getIntValue() to yMatches[0].value.getIntValue()
            val beacon = xMatches[1].value.getIntValue() to yMatches[1].value.getIntValue()
            Data(sensor, beacon)
        }
}

private fun Data.getExcludedXRangeOrNull(y: Int, coerceIn: IntRange): IntRange? {
    val excludedXCount = getExcludedXCountFor(y)
    if (excludedXCount < 0) return null

    return sensor.first - excludedXCount..sensor.first + excludedXCount.coerceIn(coerceIn)
}

private fun Data.getExcludedXCountFor(y: Int): Int {
    return radius - abs(sensor.second - y)
}

private data class Data(
    val sensor: Pair<Int, Int>,
    val beacon: Pair<Int, Int>
) {
    val radius = sensor distanceTo beacon
}

fun Pair<Int, Int>.canBeBeacon(
    sensor: Pair<Int, Int>,
    beacon: Pair<Int, Int>,
    closestBeaconRadius: Int
): Boolean {
    val distanceToSensor = this distanceTo sensor

    return (distanceToSensor > closestBeaconRadius || this == beacon) && this != sensor
}

/**
 * For x=123 returns 123
 * For y=234 returns 234
 */
private fun String.getIntValue(): Int {
    return drop(2).toInt()
}

infix fun Pair<Int, Int>.distanceTo(other: Pair<Int, Int>): Int {
    return abs(first - other.first) + abs(second - other.second)
}
