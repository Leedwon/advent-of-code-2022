package day2

import util.readFileLines

private const val inputFile = "/day2.txt"

fun solveDay21() {
    val lines = readFileLines(inputFile)
    val rounds = parseInputForChallenge1(lines)
    val score = calculateGameScore(rounds)
    println(score)
}

fun solveDay22() {
    val lines = readFileLines(inputFile)
    val rounds = parseInputForChallenge2(lines)
    val score = calculateGameScore(rounds)
    println(score)
}

private fun calculateGameScore(rounds: List<Round>): Int {
    return rounds.sumOf { it.myMove.scoreAgainst(it.opponentMove) }
}

private fun parseInputForChallenge1(lines: List<String>): List<Round> =
    lines
        .map { it.split(" ") }
        .map {
            Round(
                opponentMove = it[0].opponentMove(),
                myMove = it[1].myMove()
            )
        }

private fun parseInputForChallenge2(lines: List<String>): List<Round> =
    lines
        .map { it.split(" ") }
        .map {
            val opponentMove = it[0].opponentMove()
            val expectedOutcome = it[1].expectedOutcome()

            Round(
                opponentMove = opponentMove,
                myMove = expectedOutcome.findMove(opponentMove)
            )
        }

private fun String.opponentMove(): Move = when (this) {
    "A" -> Move.Rock
    "B" -> Move.Paper
    "C" -> Move.Scissors
    else -> error("invalid opponent's move")
}

private fun String.myMove(): Move = when (this) {
    "X" -> Move.Rock
    "Y" -> Move.Paper
    "Z" -> Move.Scissors
    else -> error("invalid my move")
}

private fun String.expectedOutcome(): Outcome = when (this) {
    "X" -> Outcome.Lose
    "Y" -> Outcome.Draw
    "Z" -> Outcome.Win
    else -> error("invalid expected outcome")
}

private fun Move.scoreAgainst(other: Move): Int {
    return when (this) {
        Move.Paper -> when (other) {
            Move.Paper -> 3
            Move.Rock -> 6
            Move.Scissors -> 0
        }
        Move.Rock -> when (other) {
            Move.Paper -> 0
            Move.Rock -> 3
            Move.Scissors -> 6
        }
        Move.Scissors -> when (other) {
            Move.Paper -> 6
            Move.Rock -> 0
            Move.Scissors -> 3
        }
    } + this.score
}

private fun Outcome.findMove(other: Move): Move {
    return when (this) {
        Outcome.Draw -> other
        Outcome.Lose -> other.winsAgainst()
        Outcome.Win -> other.losesTo()
    }
}

private fun Move.losesTo(): Move {
    return when (this) {
        Move.Paper -> Move.Scissors
        Move.Rock -> Move.Paper
        Move.Scissors -> Move.Rock
    }
}

private fun Move.winsAgainst(): Move {
    return when (this) {
        Move.Paper -> Move.Rock
        Move.Rock -> Move.Scissors
        Move.Scissors -> Move.Paper
    }
}

private data class Round(
    val opponentMove: Move,
    val myMove: Move
)

private sealed class Move {
    abstract val score: Int

    object Rock : Move() {
        override val score: Int = 1
    }

    object Paper : Move() {
        override val score: Int = 2
    }

    object Scissors : Move() {
        override val score: Int = 3
    }
}

private sealed class Outcome {
    object Win : Outcome()
    object Draw : Outcome()
    object Lose : Outcome()
}