package day14

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class RegolithReservoirTest {

    @Test
    fun `should parse rock path`() {
        val input = """
            503,4 -> 502,4 -> 502,9 -> 494,9
        """.trimIndent()

        val expected = listOf(
            Position(
                x = 503,
                y = 4,
            ),
            Position(
                x = 502,
                y = 4,
            )
        ) + List(5) {
            Position(
                x = 502,
                y = it + 4 + 1,
            )
        } + List(8) {
            Position(
                x = 502 - it - 1,
                y = 9,
            )
        }

        val actual = input.parseRockPath()

        assertEquals(expected, actual)
    }

    @Test
    fun `should initialize cave`() {
        val input = """
            498,4 -> 498,6 -> 496,6
            503,4 -> 502,4 -> 502,9 -> 494,9
        """.trimIndent().split("\n")

        val expected = mapOf(
            Position(494, 0) to Type.Air,
            Position(494, 1) to Type.Air,
            Position(494, 2) to Type.Air,
            Position(494, 3) to Type.Air,
            Position(494, 4) to Type.Air,
            Position(494, 5) to Type.Air,
            Position(494, 6) to Type.Air,
            Position(494, 7) to Type.Air,
            Position(494, 8) to Type.Air,
            Position(494, 9) to Type.Rock,

            Position(495, 0) to Type.Air,
            Position(495, 1) to Type.Air,
            Position(495, 2) to Type.Air,
            Position(495, 3) to Type.Air,
            Position(495, 4) to Type.Air,
            Position(495, 5) to Type.Air,
            Position(495, 6) to Type.Air,
            Position(495, 7) to Type.Air,
            Position(495, 8) to Type.Air,
            Position(495, 9) to Type.Rock,

            Position(496, 0) to Type.Air,
            Position(496, 1) to Type.Air,
            Position(496, 2) to Type.Air,
            Position(496, 3) to Type.Air,
            Position(496, 4) to Type.Air,
            Position(496, 5) to Type.Air,
            Position(496, 6) to Type.Rock,
            Position(496, 7) to Type.Air,
            Position(496, 8) to Type.Air,
            Position(496, 9) to Type.Rock,

            Position(497, 0) to Type.Air,
            Position(497, 1) to Type.Air,
            Position(497, 2) to Type.Air,
            Position(497, 3) to Type.Air,
            Position(497, 4) to Type.Air,
            Position(497, 5) to Type.Air,
            Position(497, 6) to Type.Rock,
            Position(497, 7) to Type.Air,
            Position(497, 8) to Type.Air,
            Position(497, 9) to Type.Rock,

            Position(498, 0) to Type.Air,
            Position(498, 1) to Type.Air,
            Position(498, 2) to Type.Air,
            Position(498, 3) to Type.Air,
            Position(498, 4) to Type.Rock,
            Position(498, 5) to Type.Rock,
            Position(498, 6) to Type.Rock,
            Position(498, 7) to Type.Air,
            Position(498, 8) to Type.Air,
            Position(498, 9) to Type.Rock,

            Position(499, 0) to Type.Air,
            Position(499, 1) to Type.Air,
            Position(499, 2) to Type.Air,
            Position(499, 3) to Type.Air,
            Position(499, 4) to Type.Air,
            Position(499, 5) to Type.Air,
            Position(499, 6) to Type.Air,
            Position(499, 7) to Type.Air,
            Position(499, 8) to Type.Air,
            Position(499, 9) to Type.Rock,

            Position(500, 0) to Type.Entrance,
            Position(500, 1) to Type.Air,
            Position(500, 2) to Type.Air,
            Position(500, 3) to Type.Air,
            Position(500, 4) to Type.Air,
            Position(500, 5) to Type.Air,
            Position(500, 6) to Type.Air,
            Position(500, 7) to Type.Air,
            Position(500, 8) to Type.Air,
            Position(500, 9) to Type.Rock,

            Position(501, 0) to Type.Air,
            Position(501, 1) to Type.Air,
            Position(501, 2) to Type.Air,
            Position(501, 3) to Type.Air,
            Position(501, 4) to Type.Air,
            Position(501, 5) to Type.Air,
            Position(501, 6) to Type.Air,
            Position(501, 7) to Type.Air,
            Position(501, 8) to Type.Air,
            Position(501, 9) to Type.Rock,

            Position(502, 0) to Type.Air,
            Position(502, 1) to Type.Air,
            Position(502, 2) to Type.Air,
            Position(502, 3) to Type.Air,
            Position(502, 4) to Type.Rock,
            Position(502, 5) to Type.Rock,
            Position(502, 6) to Type.Rock,
            Position(502, 7) to Type.Rock,
            Position(502, 8) to Type.Rock,
            Position(502, 9) to Type.Rock,

            Position(503, 0) to Type.Air,
            Position(503, 1) to Type.Air,
            Position(503, 2) to Type.Air,
            Position(503, 3) to Type.Air,
            Position(503, 4) to Type.Rock,
            Position(503, 5) to Type.Air,
            Position(503, 6) to Type.Air,
            Position(503, 7) to Type.Air,
            Position(503, 8) to Type.Air,
            Position(503, 9) to Type.Air,
        )

        val actual = initializeCave(input)

        assertEquals(Cave(expected), actual)
    }
}