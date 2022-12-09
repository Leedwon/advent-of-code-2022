package day7

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class NoSpaceLeftOnDeviceTest {

    @Test
    fun `should calculate correct sum`() {
        val input = """
            $ cd /
            $ ls
            dir a
            14848514 b.txt
            8504156 c.dat
            dir d
            $ cd a
            $ ls
            dir e
            29116 f
            2557 g
            62596 h.lst
            $ cd e
            $ ls
            584 i
            200 j
            $ cd ..
            $ cd ..
            $ cd d
            $ ls
            4060174 j
            8033020 d.log
            5626152 d.ext
            7214296 k
        """.trimIndent().split("\n")

        val actual = buildFileSystem(input).totalSizeOfDirectoriesNoGreaterThan(100_000)
        val expected = 95837
        assertEquals(expected, actual)
    }
}
