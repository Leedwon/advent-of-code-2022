package day5

import kotlin.test.Test
import kotlin.test.assertEquals

class SupplyStacksTest {

    @Test
    fun `should parse stacks`() {
        val input = listOf(
            "[T]     [Q]",
            "[R]     [M]",
            "[D] [V] [V]",
        )

        val expected = listOf(
            ArrayDeque(listOf("T", "R", "D")),
            ArrayDeque(listOf("V")),
            ArrayDeque(listOf("Q", "M", "V")),
        )

        val actual = parseStacks(input)

        assertEquals(expected, actual)
    }

    @Test
    fun `should parse moves`() {
        val input = listOf(
            "move 5 from 4 to 9",
            "move 3 from 5 to 1",
            "move 12 from 9 to 6",
        )

        val expected = listOf(
            Move(howMany = 5, fromStack = 4, toStack = 9),
            Move(howMany = 3, fromStack = 5, toStack = 1),
            Move(howMany = 12, fromStack = 9, toStack = 6),
        )

        val actual = parseMoves(input)

        assertEquals(expected, actual)
    }

    @Test
    fun `should rearrange stacks without retained order`() {
        val input = listOf(
            ArrayDeque(listOf("T", "R", "D")),
            ArrayDeque(listOf("V")),
            ArrayDeque(listOf("Q", "M", "V")),
        )

        val moves = listOf(
            Move(howMany = 2, fromStack = 1, toStack = 2),
            Move(howMany = 1, fromStack = 2, toStack = 3),
        )

        input.rearrangeStacks(moves, retainOrder = false)

        val expected = listOf(
            ArrayDeque(listOf("D")),
            ArrayDeque(listOf("T", "V")),
            ArrayDeque(listOf("R", "Q", "M", "V")),
        )

        assertEquals(expected, input)
    }

    @Test
    fun `should rearrange stacks with retained order`() {
        val input = listOf(
            ArrayDeque(listOf("T", "R", "D")),
            ArrayDeque(listOf("V")),
            ArrayDeque(listOf("Q", "M", "V")),
        )

        val moves = listOf(
            Move(howMany = 2, fromStack = 1, toStack = 2),
            Move(howMany = 1, fromStack = 2, toStack = 3),
        )

        input.rearrangeStacks(moves, retainOrder = true)

        val expected = listOf(
            ArrayDeque(listOf("D")),
            ArrayDeque(listOf("R", "V")),
            ArrayDeque(listOf("T", "Q", "M", "V")),
        )

        assertEquals(expected, input)
    }
}
