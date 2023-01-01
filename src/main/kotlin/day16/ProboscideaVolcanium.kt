package day16

import util.readFileLines

fun solveDay161() {
    val lines = readFileLines("/day16.txt")
    val sum = sumValves(lines)
}

fun sumValves(lines: List<String>): Int {
    val valves = buildValvesGraph(lines)

    val startingValve = valves.first { it.name == "AA" }

    var sum = 0
    var runningFlow = startingValve.flowRate
    var valve = startingValve

    repeat(30) {
        sum += runningFlow
        if (valve.flowRate == 0 || valve.opened) {
            valve
                .routes
                .map { name -> valves.first { it.name == name } }
                .filter { !it.opened }
                .maxByOrNull { it.flowRate }
                ?.let { valve = it }
        } else {
            runningFlow += valve.flowRate
            valve.opened = true
        }
    }

    return sum
}

fun buildValvesGraph(lines: List<String>): List<Valve> = lines.map { it.parseValve() }

private fun String.parseValve(): Valve = Valve(
    name = getValveName(),
    flowRate = getFlowRate(),
    routes = getRoutes()
)

private fun String.getValveName(): String = drop(6).take(2)

private fun String.getFlowRate(): Int = split(";").first().split("=").last().toInt()

private fun String.getRoutes(): List<String> =
    split(",").map { it.takeLast(2) }

data class Valve(
    val name: String,
    val flowRate: Int,
    val routes: List<String>,
    var opened: Boolean = false
)
