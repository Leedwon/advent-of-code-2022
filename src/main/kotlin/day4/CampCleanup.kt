package day4

import util.readFileLines

private const val fileName = "/day4.txt"

fun solveDay41() {
    readFileLines(fileName)
        .toCampCleaningRanges()
        .count { it.first.contains(it.second) || it.second.contains(it.first) }
        .let { println(it) }
}

fun solveDay42() {
    readFileLines(fileName)
        .toCampCleaningRanges()
        .count { it.first.overlaps(it.second) || it.second.overlaps(it.first) }
        .let { println(it) }
}

private fun List<String>.toCampCleaningRanges(): List<Pair<IntRange, IntRange>> =
    this
        .map { it.split(",") }
        .map { it[0].toIntRange() to it[1].toIntRange() }

/**
 * Converts "1-2" to IntRange(1,2)
 */
private fun String.toIntRange(): IntRange {
    val (first, second) = this.split("-")
    return IntRange(first.toInt(), second.toInt())
}

private fun IntRange.contains(other: IntRange): Boolean {
    val startIn = other.first >= this.first
    val endIn = other.last <= this.last
    return startIn && endIn
}

private fun IntRange.overlaps(other: IntRange): Boolean {
    return this.contains(other.first) || this.contains(other.last) || other.contains(this.first) || other.contains(this.last)
}