package day15

import util.readFileLines
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

private const val fileName = "/day15.txt"

fun solveDay151() {
    val lines = readFileLines(fileName)

    val xRegex = Regex("x=.\\d*")
    val yRegex = Regex("y=.\\d*")

    val y = 2_000_000

    val data = lines
        .map {
            val xMatches = xRegex.findAll(it).toList()
            val yMatches = yRegex.findAll(it).toList()

            val sensor = xMatches[0].value.getIntValue() to yMatches[0].value.getIntValue()
            val beacon = xMatches[1].value.getIntValue() to yMatches[1].value.getIntValue()
            Data(sensor, beacon)
        }

    val minX = data.minOf { min(it.sensor.first - it.radius, it.beacon.first) }
    val maxX = data.maxOf { max(it.sensor.first + it.radius, it.beacon.first) }

    val sensorBeaconRadius = data.map { (sensor, beacon) -> (sensor to beacon) to sensor.distanceTo(beacon) }

    val result = (minX..maxX).count {
        val position = it to y

        sensorBeaconRadius.any { (sensorToBeacon, radius) ->
            val (sensor, beacon) = sensorToBeacon
            !position.canBeBeacon(sensor = sensor, beacon = beacon, closestBeaconRadius = radius)
        }
    }

    println(result)
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
