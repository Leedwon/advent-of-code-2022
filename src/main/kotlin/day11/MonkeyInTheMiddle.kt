package day11

import util.readFileLines

private const val fileName = "/day11.txt"

fun solveDay111() {
    val input = readFileLines(fileName)

    val monkeys = input.parseMonkeys()

    val result = calculateMonkeyBusiness(monkeys, 20, 3)
    println(result)
}

fun solveDay112() {
    val input = readFileLines(fileName)

    val monkeys = input.parseMonkeys()

    val result = calculateMonkeyBusiness(monkeys, 10_000, 1)
    println(result)
}

fun calculateMonkeyBusiness(monkeys: List<Monkey>, rounds: Int, calmingFactor: Int): Int {
    repeat(rounds) {
        monkeys.forEach { monkey ->
            val thrownItems = monkey.throwItems(calmingFactor)
            thrownItems.forEach { item ->
                monkeys.first { it.id == item.monkeyId }.addItem(item)
            }
        }
    }

    return monkeys
        .map { it.inspectionCount }
        .sorted()
        .takeLast(2)
        .reduce { acc, i -> acc * i }
}

data class Monkey(
    val id: Int,
    val items: ArrayDeque<Int>,
    val worryLevelFactory: WorryLevelFactory,
    val divisionTest: DivisionTest
) {

    var inspectionCount = 0
        private set

    private fun throwItem(calmingFactor: Int): ThrowData? {
        val item = items.removeFirstOrNull() ?: return null
        val worryLevel = worryLevelFactory.newWorryLevel(item).floorDiv(calmingFactor)
        val monkeyId = divisionTest.performTest(worryLevel)

        inspectionCount++
        return ThrowData(
            item = worryLevel,
            monkeyId = monkeyId
        )
    }

    fun throwItems(calmingFactor: Int): List<ThrowData> {
        val result = mutableListOf<ThrowData>()
        while (items.isNotEmpty()) {
            throwItem(calmingFactor)?.let { result.add(it) }
        }
        return result
    }

    fun addItem(throwData: ThrowData) {
        items.addLast(throwData.item)
    }
}

data class ThrowData(
    val item: Int,
    val monkeyId: Int
)

fun List<String>.parseMonkeys(): List<Monkey> {
    return this
        .chunked(7)
        .map { it.parseMonkey() }
}

fun List<String>.parseMonkey(): Monkey {
    return Monkey(
        id = this[0].parseMonkeyId(),
        items = this[1].parseMonkeyItems(),
        worryLevelFactory = this[2].parseMonkeyWorryLevelFactory(),
        divisionTest = this.drop(3).parseDivisionTest()
    )
}

fun String.parseMonkeyId(): Int {
    return this.dropLast(1).last().toString().toInt()
}

fun String.parseMonkeyItems(): ArrayDeque<Int> {
    val initialState = this.split(":")[1].trim().split(",").map { it.trim().toInt() }
    return ArrayDeque(initialState)
}

fun String.parseMonkeyWorryLevelFactory(): WorryLevelFactory {
    val data = this.split(":")[1].trim()
    val rightEquationSide = data.split("=")[1].trim()
    val (first, operation, second) = rightEquationSide.split(" ")
    return WorryLevelFactory(
        firstParamParsingStrategy = first.parseParamParsingStrategy(),
        secondParamParsingStrategy = second.parseParamParsingStrategy(),
        operation = operation.parseOperation()
    )
}

fun List<String>.parseDivisionTest(): DivisionTest {
    val (divisibleBy, onTrue, onFalse) = this.mapNotNull { it.trim().split(" ").last().toIntOrNull() }
    return DivisionTest(
        divisibleBy,
        onTrue = onTrue,
        onFalse = onFalse,
    )
}

private fun String.parseParamParsingStrategy(): WorryLevelFactory.ParamParsingStrategy {
    return when (this) {
        "old" -> WorryLevelFactory.ParamParsingStrategy.Old
        else -> WorryLevelFactory.ParamParsingStrategy.Value(this.toInt())
    }
}

private fun String.parseOperation(): Operation {
    return when (this) {
        "+" -> Operation.Addition
        "-" -> Operation.Subtraction
        "*" -> Operation.Multiplication
        "/" -> Operation.Division
        else -> error("Invalid operation")
    }
}

data class DivisionTest(
    private val divisibleBy: Int,
    private val onTrue: Int,
    private val onFalse: Int,
) {
    /**
     * @return id of monkey to which this monkey throws items
     */
    fun performTest(worryLevel: Int): Int {
        return if (worryLevel % divisibleBy == 0) onTrue else onFalse
    }
}

// Takes current worry leve and applies operation to it
data class WorryLevelFactory(
    private val firstParamParsingStrategy: ParamParsingStrategy,
    private val secondParamParsingStrategy: ParamParsingStrategy,
    private val operation: Operation
) {

    fun newWorryLevel(oldWorryLevel: Int): Int {
        val a = firstParamParsingStrategy.getValue(oldWorryLevel)
        val b = secondParamParsingStrategy.getValue(oldWorryLevel)
        return operation.execute(a, b)
    }

    sealed class ParamParsingStrategy {
        object Old : ParamParsingStrategy()
        data class Value(val value: Int) : ParamParsingStrategy()

        fun getValue(oldWorryLevel: Int): Int {
            return when (this) {
                Old -> oldWorryLevel
                is Value -> value
            }
        }
    }
}

sealed interface Operation {
    fun execute(a: Int, b: Int): Int

    object Addition : Operation {
        override fun execute(a: Int, b: Int): Int = a + b
    }

    object Subtraction : Operation {
        override fun execute(a: Int, b: Int): Int = a - b
    }

    object Multiplication : Operation {
        override fun execute(a: Int, b: Int): Int = a * b
    }

    object Division : Operation {
        override fun execute(a: Int, b: Int): Int = a / b
    }
}
