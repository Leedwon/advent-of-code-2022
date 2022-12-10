package day10

import util.readFileLines

private const val fileName = "/day10.txt"

fun solveDay101() {
    val lines = readFileLines(fileName)

    val cpu = Cpu()

    val commands = lines.map { it.parseCommand() }

    val result = cpu.calculateSignalStrength(commands)
    println(result)
}

fun solveDay102() {
    val lines = readFileLines(fileName)

    val cpu = Cpu()

    val commands = lines.map { it.parseCommand() }

    val result = cpu.drawSprites(commands)
    println(result)
}

fun Cpu.calculateSignalStrength(commands: List<Cpu.Command>): Int {
    execute(commands)

    val cyclesIncrement = listOf(20, 40, 40, 40, 40, 40)

    return cyclesIncrement.sumOf {
        tick(it) { cpuData ->
            cpuData.register * cpuData.cycle
        }
    }
}

fun Cpu.drawSprites(commands: List<Cpu.Command>): String {
    val columns = 40
    val rows = 6

    execute(commands)

    return buildString {
        repeat(rows) { row ->
            repeat(columns) {
                tick {
                    println("row = $row data = $it")
                    if ((it.cycle - 1) % columns in (it.register - 1..it.register + 1)) {
                        append("#")
                    } else {
                        append(".")
                    }
                }
            }
            append("\n")
        }
    }
}

fun String.parseCommand(): Cpu.Command {
    return when {
        this.startsWith("noop") -> Cpu.Command.NoOp
        this.startsWith("addx") -> {
            val (_, value) = this.split(" ")
            return Cpu.Command.Add(value.toInt())
        }

        else -> error("Invalid command")
    }
}

class Cpu {

    private val stack = ArrayDeque<Command>()
    private var register = 1
    private var cycle = 1

    private var runningCommand: Command? = null
    private var cyclesForCommandLeft = 0

    fun reset() {
        cycle = 1
        register = 1
        stack.clear()
    }

    fun <T> tick(times: Int, lookup: (Data) -> T): T {
        return List(times) { it }.map { index ->
            tick {
                if (index == times - 1) {
                    lookup(it)
                } else {
                    null
                }
            }
        }.firstNotNullOf { it }
    }

    fun <T> tick(lookup: (Data) -> T): T {
        if (runningCommand == null) {
            runningCommand = stack.removeFirstOrNull()
            cyclesForCommandLeft = runningCommand?.executionCycles ?: 0
        }

        cyclesForCommandLeft = cyclesForCommandLeft.dec().coerceAtLeast(0)

        val result = lookup(Data(register, cycle)).also { cycle++ }
        if (cyclesForCommandLeft == 0) {
            runningCommand?.let { command ->
                when (command) {
                    is Command.Add -> register += command.value
                    Command.NoOp -> {
                        // no-op
                    }
                }
            }
            runningCommand = null
        }

        return result
    }

    fun execute(commands: List<Command>) {
        stack.addAll(commands)
    }

    sealed class Command {

        abstract val executionCycles: Int

        object NoOp : Command() {
            override val executionCycles: Int = 1
        }

        data class Add(val value: Int) : Command() {
            override val executionCycles: Int = 2
        }
    }

    data class Data(
        val register: Int,
        val cycle: Int
    )
}


