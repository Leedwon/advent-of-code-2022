import day3.sumPriorities
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class RucksackReorganizationTest {

    @Test
    fun `should calculated correct value for test puzzle`() {
        val input = listOf(
            "vJrwpWtwJgWrhcsFMMfFFhFp",
            "jqHRNqRjqzjGDLGLrsFMfFZSrLrFZsSL",
            "PmmdzqPrVvPwwTWBwg",
            "wMqvLMZHhHMvwLHjbvcjnnSBnvTQFn",
            "ttgJtRGJQctTZtZT",
            "CrZsJsPPZsGzwwsLwLmpwMDw",
        )

        val actual = sumPriorities(input)
        val expected = 157
        assertEquals(expected, actual)
    }
}
