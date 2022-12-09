package day8

import util.readFileLines

private const val fileName = "/day8.txt"

fun solveDay81() {
    val lines = readFileLines(fileName)
    val sum = countVisibleTrees(lines)
    println(sum)
}

fun solveDay82() {
    val lines = readFileLines(fileName)
    val bestTree = countBestTree(lines)
    println(bestTree)
}

fun countVisibleTrees(lines: List<String>): Int {
    val columnSize = lines[0].length
    val values = lines.map { it.map { digit -> digit.toString().toInt() } }.flatten()

    val arr = values.toArray2d(columnSize = columnSize)

    var sum = 0
    arr.forEachIndexed { column, row, value ->
        val predicate: (Int) -> Boolean = { it < value }
        val isVisible =
            (arr.allOnLeft(column, row, predicate) ||
                    arr.allOnRight(column, row, predicate) ||
                    arr.allOnTop(column, row, predicate) ||
                    arr.allBelow(column, row, predicate))
        if (isVisible) sum++
    }
    return sum
}

fun countBestTree(lines: List<String>): Int {
    val columnSize = lines[0].length
    val values = lines.map { it.map { digit -> digit.toString().toInt() } }.flatten()

    val arr = values.toArray2d(columnSize = columnSize)

    var bestScore = 0
    arr.forEachIndexed { column, row, value ->
        val stop: (Int) -> Boolean = { it >= value }
        val score =
            arr.countLeft(column, row, stop) *
                    arr.countRight(column, row, stop) *
                    arr.countTop(column, row, stop) *
                    arr.countBottom(column, row, stop)

        if (score > bestScore) {
            bestScore = score
        }
    }
    return bestScore
}

private fun <T> Array2d<T>.countLeft(startingColumn: Int, startingRow: Int, stop: (T) -> Boolean): Int {
    var sum = 0
    var column = startingColumn - 1
    while (column >= 0) {
        sum++
        if (stop(get(column = column, row = startingRow))) return sum
        column--
    }

    return sum
}


private fun <T> Array2d<T>.countRight(startingColumn: Int, startingRow: Int, stop: (T) -> Boolean): Int {
    var sum = 0
    var column = startingColumn + 1
    while (column < columnSize) {
        sum++
        if (stop(get(column = column, row = startingRow))) return sum
        column++
    }

    return sum
}

private fun <T> Array2d<T>.countTop(startingColumn: Int, startingRow: Int, stop: (T) -> Boolean): Int {
    var sum = 0
    var row = startingRow - 1
    while (row >= 0) {
        sum++
        if (stop(get(column = startingColumn, row = row))) return sum
        row--
    }

    return sum
}


private fun <T> Array2d<T>.countBottom(startingColumn: Int, startingRow: Int, stop: (T) -> Boolean): Int {
    var sum = 0
    var row = startingRow + 1
    while (row < rowSize) {
        sum++
        if (stop(get(column = startingColumn, row = row))) return sum
        row++
    }

    return sum
}


private fun <T> Array2d<T>.allOnLeft(startingColumn: Int, startingRow: Int, predicate: (T) -> Boolean): Boolean {
    var column = startingColumn - 1
    while (column >= 0) {
        if (!predicate(get(column = column, row = startingRow))) return false
        column--
    }

    return true
}

private fun <T> Array2d<T>.allOnRight(startingColumn: Int, startingRow: Int, predicate: (T) -> Boolean): Boolean {
    var column = startingColumn + 1
    while (column < columnSize) {
        if (!predicate(get(column = column, row = startingRow))) return false
        column++
    }

    return true
}

private fun <T> Array2d<T>.allOnTop(startingColumn: Int, startingRow: Int, predicate: (T) -> Boolean): Boolean {
    var row = startingRow - 1
    while (row >= 0) {
        if (!predicate(get(column = startingColumn, row = row))) return false
        row--
    }

    return true
}


private fun <T> Array2d<T>.allBelow(startingColumn: Int, startingRow: Int, predicate: (T) -> Boolean): Boolean {
    var row = startingRow + 1
    while (row < rowSize) {
        if (!predicate(get(column = startingColumn, row = row))) return false
        row++
    }

    return true
}

private fun <T> List<T>.toArray2d(columnSize: Int) = Array2d(input = this, columnSize = columnSize)

private class Array2d<T>(private val input: List<T>, val columnSize: Int) {

    val rowSize = input.size / columnSize

    private fun isColumnInArray(column: Int): Boolean =
        column in 0 until columnSize

    private fun isRowInArray(row: Int): Boolean =
        row in 0 until rowSize

    private fun indexToRow(index: Int): Int = index / columnSize

    private fun indexToColumn(index: Int): Int = index % columnSize

    fun get(column: Int, row: Int): T {
        check(isColumnInArray(column) && isRowInArray(row)) { "Provide column and row from within array" }
        val index = row * columnSize + column
        return input[index]
    }

    fun getOrNull(column: Int, row: Int): T? {
        val index = row * columnSize + column
        return if (isColumnInArray(column) && isRowInArray(row)) {
            input[index]
        } else {
            null
        }
    }

    fun forEachIndexed(operation: (column: Int, row: Int, value: T) -> Unit) {
        input.forEachIndexed { index, element ->
            operation(indexToColumn(index), indexToRow(index), element)
        }
    }
}
