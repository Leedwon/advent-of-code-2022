package day13

import util.readFileLines
import java.lang.StringBuilder

private const val fileName = "/day13.txt"

fun solveDay131() {
    val lines = readFileLines(fileName)

    val result = calculateOrderedPacketsSum(lines)

    println(result)
}

fun solveDay132() {
    val lines = readFileLines(fileName)

    val additionalPacket1 = dataListOf(PacketData.Value(2))
    val additionalPacket2 = dataListOf(PacketData.Value(6))

    val sorted = lines
        .asSequence()
        .filter { it.isNotEmpty() }
        .map { PacketData.DataList(it.parsePacketLine()) }
        .plus(additionalPacket1)
        .plus(additionalPacket2)
        .sortedWith(PacketDataComparator)
        .toList()

    val result = (sorted.indexOf(additionalPacket1) + 1) * (sorted.indexOf(additionalPacket2) + 1)
    println(result)
}

private object PacketDataComparator : Comparator<PacketData> {
    override fun compare(o1: PacketData, o2: PacketData): Int {
        return when (o1.orderAgainst(o2)) {
            Order.Correct -> -1
            Order.Incorrect -> 1
            Order.Undefined -> 0
        }
    }

}

fun calculateOrderedPacketsSum(lines: List<String>): Int {
    return lines
        .asSequence()
        .filter { it.isNotEmpty() }
        .chunked(2)
        .map {
            Packet(
                first = it[0].parsePacketLine(),
                second = it[1].parsePacketLine()
            )
        }
        .mapIndexed { index, packet -> if (packet.isInOrder()) index + 1 else 0 }
        .sum()
}

data class Packet(
    val first: List<PacketData>,
    val second: List<PacketData>
) {
    fun isInOrder(): Boolean {
        val zipped = first.zip(second)

        zipped.forEach {
            val (first, second) = it

            val order = first.orderAgainst(second)
            if (order.defined) {
                return order.correct
            }
        }
        return first.size < second.size
    }
}

private enum class Order {
    Correct,
    Incorrect,
    Undefined
}

private val Order.defined: Boolean
    get() = when (this) {
        Order.Correct,
        Order.Incorrect -> true

        Order.Undefined -> false
    }

private val Order.correct: Boolean
    get() = when (this) {
        Order.Correct -> true
        Order.Incorrect -> false
        Order.Undefined -> error("Order undefined")
    }

private fun PacketData.orderAgainst(other: PacketData): Order {
    return when {
        this is PacketData.Value && other is PacketData.Value -> this.orderAgainst(other)
        this is PacketData.DataList && other is PacketData.DataList -> this.orderAgainst(other)
        this is PacketData.Value && other is PacketData.DataList -> dataListOf(this).orderAgainst(other)
        this is PacketData.DataList && other is PacketData.Value -> this.orderAgainst(dataListOf(other))
        else -> error("Can't compute order against for this setup")
    }
}

private fun PacketData.Value.orderAgainst(other: PacketData.Value): Order {
    return when {
        value < other.value -> Order.Correct
        value > other.value -> Order.Incorrect
        else -> Order.Undefined
    }
}

private fun PacketData.DataList.orderAgainst(other: PacketData.DataList): Order {
    val firstStack = ArrayDeque(this.values)
    val secondStack = ArrayDeque(other.values)

    while (firstStack.isNotEmpty() && secondStack.isNotEmpty()) {
        val first = firstStack.removeFirst()
        val second = secondStack.removeFirst()

        val order = first.orderAgainst(second)
        if (order.defined) return order
    }

    return when {
        firstStack.isEmpty() && secondStack.isEmpty() -> Order.Undefined
        firstStack.isEmpty() -> Order.Correct
        secondStack.isEmpty() -> Order.Incorrect
        else -> error("This shouldn't happen")
    }
}

fun String.parsePacket(): Packet {
    val (first, second) = split("\n")
    return Packet(
        first = first.parsePacketLine(),
        second = second.parsePacketLine()
    )
}

private fun String.parsePacketLine(): List<PacketData> {
    val unwrapped = unwrapped()
    val stack = ArrayDeque(unwrapped.map { it })

    val data = mutableListOf<PacketData>()
    while (stack.isNotEmpty()) {
        val next = stack.removeFirst()
        when {
            next.isDigit() -> {
                stack.addFirst(next)
                data.add(parseIntValue(stack))
            }

            next == '[' -> data.add(parseDataList(stack))
            next == ',' -> {
                // no-op commas are skipped - maybe remove them earlier on?
            }

            else -> error("invalid packet line")
        }
    }
    return data
}

private fun parseDataList(stack: ArrayDeque<Char>): PacketData.DataList {
    val values = mutableListOf<PacketData>()

    var next = stack.removeFirst()
    while (next != ']') {
        when {
            next.isDigit() -> {
                stack.addFirst(next)
                values.add(parseIntValue(stack))
            }

            next == '[' -> values.add(parseDataList(stack))
            next == ',' -> {
                // no-op commas are skipped - maybe remove them earlier on?
            }
        }
        next = stack.removeFirst()
    }

    return PacketData.DataList(values)
}

private fun parseIntValue(stack: ArrayDeque<Char>): PacketData.Value {
    val strBuilder = StringBuilder()
    var next: Char? = stack.removeFirst()
    while (next?.isDigit() == true) {
        strBuilder.append(next)
        next = stack.removeFirstOrNull()
    }

    next?.let { stack.addFirst(it) }

    return PacketData.Value(strBuilder.toString().toInt())
}

private fun Char.toValue(): PacketData.Value = PacketData.Value(this.toString().toInt())

/**
 * Each packet line starts with [ and ends with ]. This function removes [] leaving raw data.
 */
private fun String.unwrapped(): String {
    check(first() == '[' && last() == ']') { "invalid packet line" }

    return drop(1).dropLast(1)
}

private fun dataListOf(vararg elements: PacketData) =
    PacketData.DataList(values = if (elements.isEmpty()) emptyList() else elements.asList())

sealed interface PacketData {
    data class Value(val value: Int) : PacketData
    data class DataList(val values: List<PacketData>) : PacketData
}

private fun PacketData.prettyPrint(): String {
    return when (this) {
        is PacketData.DataList -> "[${this.values.joinToString { it.prettyPrint() }}]"
        is PacketData.Value -> "$value"
    }
}
