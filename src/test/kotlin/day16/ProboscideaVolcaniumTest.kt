package day16

import org.junit.jupiter.api.Test
import util.readFileLines
import kotlin.test.assertEquals

class ProboscideaVolcaniumTest {

    @Test
    fun `should build valves graph`() {
        val lines = readFileLines("/test_day16.txt")
        val graph = buildValvesGraph(lines)

        val expected = mapOf(
            Valve(name = "AA", flowRate = 0) to listOf(
                Valve(name = "DD", flowRate = 20),
                Valve(name = "II", flowRate = 0),
                Valve(name = "BB", flowRate = 13),
            ),
            Valve(name = "BB", flowRate = 13) to listOf(
                Valve(name = "CC", flowRate = 2),
                Valve(name = "AA", flowRate = 0),
            ),
            Valve(name = "CC", flowRate = 2) to listOf(
                Valve(name = "DD", flowRate = 20),
                Valve(name = "BB", flowRate = 13),
            ),
            Valve(name = "DD", flowRate = 20) to listOf(
                Valve(name = "CC", flowRate = 2),
                Valve(name = "AA", flowRate = 0),
                Valve(name = "EE", flowRate = 3),
            ),
            Valve(name = "EE", flowRate = 3) to listOf(
                Valve(name = "FF", flowRate = 0),
                Valve(name = "DD", flowRate = 20)
            ),
            Valve(name = "FF", flowRate = 0) to listOf(
                Valve(name = "EE", flowRate = 3),
                Valve(name = "GG", flowRate = 0)
            ),
            Valve(name = "GG", flowRate = 0) to listOf(
                Valve(name = "FF", flowRate = 0),
                Valve(name = "HH", flowRate = 22)
            ),
            Valve(name = "HH", flowRate = 22) to listOf(
                Valve(name = "GG", flowRate = 0)
            ),
            Valve(name = "II", flowRate = 0) to listOf(
                Valve(name = "AA", flowRate = 0),
                Valve(name = "JJ", flowRate = 21)
            ),
            Valve(name = "JJ", flowRate = 21) to listOf(
                Valve(name = "II", flowRate = 0),
            ),
        )

        assertEquals(expected, graph)
    }

    @Test
    fun `should calculate shortest path`() {
        val lines = readFileLines("/test_day16.txt")
        val graph = buildValvesGraph(lines)

        val actual = graph.shortestPath(
            from = Valve(name = "AA", flowRate = 0),
            to = Valve(name = "EE", flowRate = 3)
        )

        assertEquals(2, actual)
    }

    @Test
    fun `should solve test part 1`() {
        val lines = readFileLines("/test_day16.txt")
        val graph = buildValvesGraph(lines)

        val actual = graph.calculateMostPressureToRelease()

        assertEquals(1651, actual)
    }

    @Test
    fun `should solve test part 2`() {
        val lines = readFileLines("/test_day16.txt")
        val graph = buildValvesGraph(lines)

        val actual = graph.calculateMostPressureToReleaseWithElephantHelp()

        assertEquals(1707, actual)
    }

    @Test
    fun `should generate combinations`() {
        val input = listOf("A", "B", "C")

        val expected = listOf(
            listOf("A", "B"),
            listOf("A", "C"),
            listOf("B", "A"),
            listOf("B", "C"),
            listOf("C", "A"),
            listOf("C", "B"),
        )

        val actual = input.combinations(2)

        assertEquals(expected, actual)
    }
}
