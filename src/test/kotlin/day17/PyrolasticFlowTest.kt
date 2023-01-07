package day17

import org.junit.jupiter.api.Test
import util.readFileLines
import kotlin.test.assertEquals

class PyrolasticFlowTest {

    @Test
    fun `should solve day 17 part 1`() {
        val input = readFileLines("/test_day17.txt").first().parseInput()

        val actual = simulateRockFall(input)

        val expected = 3068L

        assertEquals(expected, actual)
    }

    @Test
    fun `should solve day 17 part 2`() {
        val input = readFileLines("/test_day17.txt").first().parseInput()

        val actual = simulateRockFall(input, fallenRocksLimit = 1000000000000L)

        val expected = 1514285714288L

        assertEquals(expected, actual)
    }
}