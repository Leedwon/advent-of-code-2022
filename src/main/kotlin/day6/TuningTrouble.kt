package day6

import util.readFileLines

private const val fileName = "/day6.txt"

fun solveDay61() {
    val input = readFileLines(fileName)

    val windowSize = 4
    val solution = input
        .joinToString(separator = "")
        .windowed(windowSize)
        .indexOfFirst { chunk -> chunk.all { char -> chunk.count { it == char } == 1 } }
        .plus(windowSize)

    println(solution)
}

fun solveDay62() {
    val input = readFileLines(fileName)

    val windowSize = 14
    val solution = input
        .joinToString(separator = "")
        .windowed(windowSize)
        .indexOfFirst { chunk -> chunk.all { char -> chunk.count { it == char } == 1 } }
        .plus(windowSize)

    println(solution)
}
