package day13

import org.junit.jupiter.api.Test
import kotlin.math.exp
import kotlin.test.assertEquals

class DistressSignalTest {

    @Test
    fun `should parse all integer values`() {
        val input = """
            [1,1,3,1,1]
            [1,1,5,12,10]
        """.trimIndent()

        val expected = Packet(
            first = listOf(
                1.toPacketData(),
                1.toPacketData(),
                3.toPacketData(),
                1.toPacketData(),
                1.toPacketData(),
            ),
            second = listOf(
                1.toPacketData(),
                1.toPacketData(),
                5.toPacketData(),
                12.toPacketData(),
                10.toPacketData(),
            )
        )

        val actual = input.parsePacket()

        assertEquals(expected, actual)
    }

    @Test
    fun `should parse packet with integer and simple list`() {
        val input = """
            [[1,1],3]
            [[1,1],5]
        """.trimIndent()

        val expected = Packet(
            first = listOf(
                dataListOf(1.toPacketData(), 1.toPacketData()),
                3.toPacketData(),
            ),
            second = listOf(
                dataListOf(1.toPacketData(), 1.toPacketData()),
                5.toPacketData(),
            )
        )

        val actual = input.parsePacket()

        assertEquals(expected, actual)
    }

    @Test
    fun `should parse nested lists`() {
        val input = """
           [1,[2,[3,[4,[5,6,7]]]],8,9]
           [1,[2,[3,[4,[5,6,7]]]],8,9]
        """.trimIndent()

        val packetDataList = listOf(
            1.toPacketData(),
            dataListOf(
                2.toPacketData(),
                dataListOf(
                    3.toPacketData(),
                    dataListOf(
                        4.toPacketData(),
                        dataListOf(
                            5.toPacketData(),
                            6.toPacketData(),
                            7.toPacketData(),
                        )
                    )
                )
            ),
            8.toPacketData(),
            9.toPacketData()
        )

        val expected = Packet(
            first = packetDataList,
            second = packetDataList
        )

        val actual = input.parsePacket()

        assertEquals(expected, actual)
    }

    @Test
    fun `should determine correct order for integers`() {
        val input = Packet(
            first = listOf(
                1.toPacketData(),
                1.toPacketData(),
                3.toPacketData(),
                1.toPacketData(),
                1.toPacketData(),

                ),
            second = listOf(
                1.toPacketData(),
                1.toPacketData(),
                5.toPacketData(),
                1.toPacketData(),
                1.toPacketData(),
            )
        )

        assertEquals(true, input.isInOrder())
    }

    @Test
    fun `should determine incorrect order for integers`() {
        val input = Packet(
            first = listOf(
                1.toPacketData(),
                7.toPacketData(),
                1.toPacketData(),
            ),
            second = listOf(
                1.toPacketData(),
                5.toPacketData(),
                1.toPacketData(),
            )
        )

        assertEquals(false, input.isInOrder())
    }

    @Test
    fun `should determine correct order for lists with different values`() {
        val input = Packet(
            first = listOf(
                dataListOf(
                    1.toPacketData(),
                    2.toPacketData(),
                )
            ),
            second = listOf(
                dataListOf(
                    1.toPacketData(),
                    2.toPacketData(),
                    3.toPacketData(),
                )
            )
        )

        assertEquals(true, input.isInOrder())
    }

    @Test
    fun `should determine incorrect order for lists with different values`() {
        val input = Packet(
            first = listOf(
                dataListOf(
                    2.toPacketData(),
                    3.toPacketData(),
                )
            ),
            second = listOf(
                dataListOf(
                    1.toPacketData(),
                    2.toPacketData()
                )
            )
        )

        assertEquals(false, input.isInOrder())
    }

    @Test
    fun `should determine correct order for lists of different sizes`() {
        val input = Packet(
            first = listOf(
                dataListOf(
                    1.toPacketData(),
                )
            ),
            second = listOf(
                dataListOf(
                    1.toPacketData(),
                    2.toPacketData()
                )
            )
        )

        assertEquals(true, input.isInOrder())
    }

    @Test
    fun `should determine incorrect order for lists of different sizes`() {
        val input = Packet(
            first = listOf(
                dataListOf(
                    1.toPacketData(),
                    2.toPacketData(),
                    3.toPacketData(),
                )
            ),
            second = listOf(
                dataListOf(
                    1.toPacketData(),
                    2.toPacketData()
                )
            )
        )

        assertEquals(false, input.isInOrder())
    }

    @Test
    fun `should determine incorrect order for mixed integer and list`() {
        val input = Packet(
            first = listOf(
                1.toPacketData(),
                dataListOf(
                    2.toPacketData(),
                    dataListOf(
                        3.toPacketData(),
                        dataListOf(
                            4.toPacketData(),
                            dataListOf(
                                5.toPacketData(),
                                6.toPacketData(),
                                7.toPacketData(),
                            )
                        )
                    )
                ),
                8.toPacketData(),
                9.toPacketData()
            ),
            second = listOf(
                1.toPacketData(),
                dataListOf(
                    2.toPacketData(),
                    dataListOf(
                        3.toPacketData(),
                        dataListOf(
                            4.toPacketData(),
                            dataListOf(
                                5.toPacketData(),
                                6.toPacketData(),
                                0.toPacketData(),
                            )
                        )
                    )
                ),
                8.toPacketData(),
                9.toPacketData()
            )
        )

        assertEquals(false, input.isInOrder())
    }

    @Test
    fun `empty list should be in order`() {
        val input = Packet(
            first = listOf(),
            second = listOf(
                dataListOf(
                    1.toPacketData(),
                    2.toPacketData()
                )
            )
        )

        assertEquals(true, input.isInOrder())
    }

    @Test
    fun `should calculate correct packet sum`() {
        val input = """
            [1,1,3,1,1]
            [1,1,5,1,1]

            [[1],[2,3,4]]
            [[1],4]

            [9]
            [[8,7,6]]

            [[4,4],4,4]
            [[4,4],4,4,4]

            [7,7,7,7]
            [7,7,7]

            []
            [3]

            [[[]]]
            [[]]

            [1,[2,[3,[4,[5,6,7]]]],8,9]
            [1,[2,[3,[4,[5,6,0]]]],8,9]
        """.trimIndent().split("\n")

        val actual = calculateOrderedPacketsSum(input)

        assertEquals(13, actual)
    }

    @Test
    fun `should parse`() {
        val input = """
            [[[[10,64]]]]
            []
        """.trimIndent()

        val expected = Packet(
            first = listOf(
                dataListOf(
                    dataListOf(
                        dataListOf(
                            10.toPacketData(),
                            64.toPacketData()
                        )
                    )
                )
            ),
            second = listOf()
        )

        val actual = input.parsePacket()

        assertEquals(expected, actual)
    }

    private fun Int.toPacketData() = PacketData.Value(this)

    private fun dataListOf(vararg elements: PacketData) =
        PacketData.DataList(values = if (elements.isEmpty()) emptyList() else elements.asList())
}
