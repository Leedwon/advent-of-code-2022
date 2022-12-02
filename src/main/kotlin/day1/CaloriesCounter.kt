package day1

import util.readFileLines

private const val inputFile = "/day1.txt"

fun solveDay11() {
    val calories = sumElvesCalories(inputFile)
    println(calories)
}

fun solveDay12() {
    val calories = sumElvesCalories(inputFileName = inputFile, elvesNumber = 3)
    println(calories)
}

/**
 * @param elvesNumber - number of elves with the highest amount of calories. Their calories should be counted.
 * @return sum of calories of elves with most calories. [elvesNumber] of elves will be taken into account.
 */
private fun sumElvesCalories(inputFileName: String, elvesNumber: Int = 1): Int {
    val elves = parseInput(readFileLines(inputFileName))
    return elves.sortedByDescending { it.calories }.take(elvesNumber).sumOf { it.calories }
}

private fun parseInput(lines: List<String>): List<Elf> =
    lines.fold(initial = (emptyList<Elf>() to 0)) { acc, line ->
        val (elves, runningCalories) = acc

        if (line.isEmpty()) {
            elves + Elf(runningCalories) to 0
        } else {
            elves to runningCalories + line.toInt()
        }
    }.first

private data class Elf(val calories: Int)
