package day3

import util.readFileLines

private const val fileName = "/day3.txt"

private typealias ElvesGroups = List<List<String>>
private typealias ElvesGroup = List<String>

fun solveDay31() {
    val lines = readFileLines(fileName)

    println(sumPriorities(rucksacks = lines))
}

fun solveDay32() {
    val lines = readFileLines(fileName)

    val sum = lines
        .toElvesGroups()
        .map { it.commonItem() }
        .sumOf { it.priority }

    println(sum)
}

private fun List<String>.toElvesGroups(): ElvesGroups = this.chunked(3)

private fun ElvesGroup.commonItem(): Char {
    val (first, second, third) = this
    return first.first { it in second && it in third }
}

fun sumPriorities(rucksacks: List<String>): Int =
    rucksacks.fold(0) { acc, rucksack ->
        val (firstHalf, secondHalf) = rucksack.splitInHalf()
        val sumOfDuplicates = firstHalf.filter { it in secondHalf }.toList().distinct().sumOf { it.priority }
        sumOfDuplicates + acc
    }

private fun String.splitInHalf(): Pair<String, String> {
    val half = this.length / 2
    val chunked = chunked(half)
    return chunked[0] to chunked[1]
}

private val Char.priority: Int
    get() = if (isLowerCase()) {
        this.code - 97 + 1 // 97 is 'a'.code value
    } else {
        this.code - 65 + 27 // 65 is 'A'.code value
    }
