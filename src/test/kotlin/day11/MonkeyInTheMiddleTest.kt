package day11

import org.junit.jupiter.api.Test
import util.readFileLines
import java.math.BigDecimal
import kotlin.test.assertEquals

class MonkeyInTheMiddleTest {

    private val actual = """
        Monkey 0:
            Starting items: 79, 98
            Operation: new = old * 19
            Test: divisible by 23
                If true: throw to monkey 2
                If false: throw to monkey 3
    """.trimIndent().split("\n")

    @Test
    fun `should parse monkey index`() {
        val expected = 0

        val actual = actual[0].parseMonkeyId()
        assertEquals(expected, actual)
    }

    @Test
    fun `should parse monkey items`() {
        val expected = ArrayDeque(listOf(79, 98))

        val actual = actual[1].parseMonkeyItems()
        assertEquals(expected, actual)
    }

    @Test
    fun `should parse new worry level factory`() {
        val expected = WorryLevelFactory(
            firstParamParsingStrategy = WorryLevelFactory.ParamParsingStrategy.Old,
            secondParamParsingStrategy = WorryLevelFactory.ParamParsingStrategy.Value(19),
            operation = Operation.Multiplication
        )

        val actual = actual[2].parseMonkeyWorryLevelFactory()
        assertEquals(expected, actual)
    }

    @Test
    fun `should parse test`() {
        val expected = DivisionTest(
            divisibleBy = 23,
            onTrue = 2,
            onFalse = 3,
        )

        val actual = listOf(actual[3], actual[4], actual[5]).parseDivisionTest()
        assertEquals(expected, actual)
    }

    @Test
    fun `should parse monkeys`() {
        val input = """
            Monkey 0:
  Starting items: 79, 98
  Operation: new = old * 19
  Test: divisible by 23
    If true: throw to monkey 2
    If false: throw to monkey 3

Monkey 1:
  Starting items: 54, 65, 75, 74
  Operation: new = old + 6
  Test: divisible by 19
    If true: throw to monkey 2
    If false: throw to monkey 0
        """.trimIndent().split("\n")

        val expected = listOf(
            Monkey(
                id = 0,
                items = ArrayDeque(listOf(79, 98)),
                worryLevelFactory = WorryLevelFactory(
                    firstParamParsingStrategy = WorryLevelFactory.ParamParsingStrategy.Old,
                    secondParamParsingStrategy = WorryLevelFactory.ParamParsingStrategy.Value(19),
                    operation = Operation.Multiplication
                ),
                divisionTest = DivisionTest(
                    divisibleBy = 23,
                    onTrue = 2,
                    onFalse = 3,
                )
            ),
            Monkey(
                id = 1,
                items = ArrayDeque(listOf(54, 65, 75, 74)),
                worryLevelFactory = WorryLevelFactory(
                    firstParamParsingStrategy = WorryLevelFactory.ParamParsingStrategy.Old,
                    secondParamParsingStrategy = WorryLevelFactory.ParamParsingStrategy.Value(6),
                    operation = Operation.Addition
                ),
                divisionTest = DivisionTest(
                    divisibleBy = 19,
                    onTrue = 2,
                    onFalse = 0,
                )
            ),
        )

        val actual = input.parseMonkeys()
        assertEquals(expected, actual)
    }

    @Test
    fun `should solve test case`() {
        val monkeys = readFileLines("/test_day11.txt").parseMonkeys()
        val actual = calculateMonkeyBusiness(monkeys, 20, 3)
        assertEquals(10605, actual)
    }

    @Test
    fun `should solve test case with huge numbers`() {
        val monkeys = readFileLines("/test_day11.txt").parseMonkeys()
        val actual = calculateMonkeyBusiness(monkeys, 10_000, 1)
        assertEquals(BigDecimal("2713310158"), actual.toBigDecimal())
    }
}
