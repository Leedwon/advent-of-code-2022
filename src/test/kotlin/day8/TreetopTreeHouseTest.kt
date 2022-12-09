package day8

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class TreetopTreeHouseTest {

    @Test
    fun `should solve test input task 1`() {
        val input = """
            30373
            25512
            65332
            33549
            35390
        """.trimIndent().split("\n")

        val expected = 21
        val actual = countVisibleTrees(input)
        assertEquals(expected, actual)
    }

    @Test
    fun `should solve test input task 2`() {
        val input = """
            30373
            25512
            65332
            33549
            35390
        """.trimIndent().split("\n")

        val expected = 8
        val actual = countBestTree(input)
        assertEquals(expected, actual)
    }
}