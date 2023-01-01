package day16

import org.junit.jupiter.api.Test
import util.readFileLines
import kotlin.test.assertEquals

class ProboscideaVolcaniumTest {

    @Test
    fun `should build valves graph`() {
        val lines = readFileLines("/test_day16.txt")
        val graph = buildValvesGraph(lines)

        val expected = listOf(
            Valve(
                name = "AA",
                flowRate = 0,
                routes = listOf("DD", "II", "BB")
            ),
            Valve(
                name = "BB",
                flowRate = 13,
                routes = listOf("CC", "AA")
            ),
            Valve(
                name = "CC",
                flowRate = 2,
                routes = listOf("DD", "BB")
            ),
            Valve(
                name = "DD",
                flowRate = 20,
                routes = listOf("CC", "AA", "EE")
            ),
            Valve(
                name = "EE",
                flowRate = 3,
                routes = listOf("FF", "DD")
            ),
            Valve(
                name = "FF",
                flowRate = 0,
                routes = listOf("EE", "GG")
            ),
            Valve(
                name = "GG",
                flowRate = 0,
                routes = listOf("FF", "HH")
            ),
            Valve(
                name = "HH",
                flowRate = 22,
                routes = listOf("GG")
            ),
            Valve(
                name = "II",
                flowRate = 0,
                routes = listOf("AA", "JJ")
            ),
            Valve(
                name = "JJ",
                flowRate = 21,
                routes = listOf("II")
            )
        )

        assertEquals(expected, graph)
    }

    @Test
    fun `should sum valves`() {
        val expected = 1651
        val actual = sumValves(readFileLines("/test_day16.txt"))

        assertEquals(expected, actual)
    }
}
