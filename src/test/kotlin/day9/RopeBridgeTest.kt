package day9

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class RopeBridgeTest {

    lateinit var rope: Rope

    @BeforeEach
    fun setup() {
        rope = Rope(Position(0, 0), 2)
    }

    @Test
    fun `head should move right`() {
        rope.move(Direction.Right)
        val actual = rope.head
        val expected = Position(1, 0)
        assertEquals(expected, actual)
    }

    @Test
    fun `head should move left`() {
        rope.move(Direction.Left)
        val actual = rope.head
        val expected = Position(-1, 0)
        assertEquals(expected, actual)
    }

    @Test
    fun `head should move up`() {
        rope.move(Direction.Up)
        val actual = rope.head
        val expected = Position(0, -1)
        assertEquals(expected, actual)
    }

    @Test
    fun `head should move down`() {
        rope.move(Direction.Down)
        val actual = rope.head
        val expected = Position(0, 1)
        assertEquals(expected, actual)
    }

    @Test
    fun `tail should move right`() {
        rope.move(Direction.Right)
        rope.move(Direction.Right)

        val actual = rope.tail
        val expected = Position(1, 0)
        assertEquals(expected, actual)
    }

    @Test
    fun `tail should move left`() {
        rope.move(Direction.Left)
        rope.move(Direction.Left)

        val actual = rope.tail
        val expected = Position(-1, 0)
        assertEquals(expected, actual)
    }


    @Test
    fun `tail should move up`() {
        rope.move(Direction.Up)
        rope.move(Direction.Up)

        val actual = rope.tail
        val expected = Position(0, -1)
        assertEquals(expected, actual)
    }


    @Test
    fun `tail should move down`() {
        rope.move(Direction.Down)
        rope.move(Direction.Down)

        val actual = rope.tail
        val expected = Position(0, 1)
        assertEquals(expected, actual)
    }

    @Test
    fun `tail should move diagonally up right`() {
        rope.move(Direction.Up)
        rope.move(Direction.Right)
        rope.move(Direction.Right)

        var actual = rope.tail
        var expected = Position(1, -1)
        assertEquals(expected, actual)

        rope = Rope(Position(0, 0), 2) // reset rope

        rope.move(Direction.Right)
        rope.move(Direction.Up)
        rope.move(Direction.Up)

        actual = rope.tail
        expected = Position(1, -1)
        assertEquals(expected, actual)
    }

    @Test
    fun `tail should move diagonally up left`() {
        rope.move(Direction.Up)
        rope.move(Direction.Left)
        rope.move(Direction.Left)

        var actual = rope.tail
        var expected = Position(-1, -1)
        assertEquals(expected, actual)

        rope = Rope(Position(0, 0), 2) // reset rope

        rope.move(Direction.Left)
        rope.move(Direction.Up)
        rope.move(Direction.Up)

        actual = rope.tail
        expected = Position(-1, -1)
        assertEquals(expected, actual)
    }

    @Test
    fun `tail should move diagonally bottom right`() {
        rope.move(Direction.Down)
        rope.move(Direction.Right)
        rope.move(Direction.Right)

        var actual = rope.tail
        var expected = Position(1, 1)
        assertEquals(expected, actual)

        rope = Rope(Position(0, 0), 2) // reset rope

        rope.move(Direction.Right)
        rope.move(Direction.Down)
        rope.move(Direction.Down)

        actual = rope.tail
        expected = Position(1, 1)
        assertEquals(expected, actual)
    }

    @Test
    fun `tail should move diagonally bottom left`() {
        rope.move(Direction.Down)
        rope.move(Direction.Left)
        rope.move(Direction.Left)

        var actual = rope.tail
        var expected = Position(-1, 1)
        assertEquals(expected, actual)

        rope = Rope(Position(0, 0), 2) // reset rope

        rope.move(Direction.Left)
        rope.move(Direction.Down)
        rope.move(Direction.Down)

        actual = rope.tail
        expected = Position(-1, 1)
        assertEquals(expected, actual)
    }


    @Test
    fun `tail should not move`() {
        val expected = Position(0, 0)

        rope.move(Direction.Right) // 1,0
        assertEquals(expected, rope.tail)

        rope.move(Direction.Up) // 1,1
        assertEquals(expected, rope.tail)

        rope.move(Direction.Left) // 0, 1
        assertEquals(expected, rope.tail)

        rope.move(Direction.Left) // -1, 1
        assertEquals(expected, rope.tail)

        rope.move(Direction.Down) // -1, 0
        assertEquals(expected, rope.tail)

        rope.move(Direction.Down) // -1, -1
        assertEquals(expected, rope.tail)

        rope.move(Direction.Right) // 0, -1
        assertEquals(expected, rope.tail)

        rope.move(Direction.Right) // 1, -1
        assertEquals(expected, rope.tail)
    }
}
