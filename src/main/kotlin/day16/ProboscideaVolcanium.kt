package day16

import util.readFileLines

typealias ValvesGraph = Map<Valve, List<Valve>>

fun solveDay161() {
    val lines = readFileLines("/day16.txt")
    val result = buildValvesGraph(lines).calculateMostPressureToRelease()
    println(result)
}

fun solveDay162() {
    val lines = readFileLines("/day16.txt")
    val result = buildValvesGraph(lines).calculateMostPressureToReleaseWithElephantHelp()
    println(result)
}

fun buildValvesGraph(lines: List<String>): ValvesGraph {
    val valvesToRoutes = lines.associate { it.parseValve() to it.getRoutes() }

    val valves = valvesToRoutes.map { it.key }

    return valvesToRoutes.mapValues { it.value.map { valves.first { valve -> valve.name == it } } }
}

fun ValvesGraph.shortestPath(from: Valve, to: Valve): Int {
    val nodes = ArrayDeque<Valve>().also { it.add(from) }
    val visited = mutableListOf<Valve>()

    val distances = mutableMapOf(from to 0)

    while (nodes.isNotEmpty()) {
        val current = nodes.removeFirst()

        for (neighbour in this[current]!!) {
            if (neighbour !in visited) {
                visited.add(neighbour)
                distances[neighbour] = distances[current]!! + 1
                nodes.add(neighbour)
            }
            if (neighbour == to) {
                return distances[to]!!
            }
        }

    }
    error("Couldn't find the shortest path")
}

fun ValvesGraph.calculateMostPressureToRelease(): Int {
    val graph = this

    data class State(
        val minutesLeft: Int,
        val openedValves: HashSet<Valve>,
        val currentValve: Valve,
        val pressureReleased: Int,
    )

    val pathsLookup: MutableMap<Pair<Valve, Valve>, Int> = mutableMapOf()

    var maxPressureRelieved = Int.MIN_VALUE

    val startingValve = keys.first { it.name == "AA" }
    val valvesToOpen = keys.filter { it.flowRate > 0 }

    val states = ArrayDeque<State>().also {
        it.add(
            State(
                minutesLeft = 30,
                openedValves = hashSetOf(),
                currentValve = startingValve,
                pressureReleased = 0
            )
        )
    }

    while (states.isNotEmpty()) {
        val state = states.removeFirst()

        for (valveToOpen in valvesToOpen - state.openedValves) {
            val shortestPath =
                pathsLookup[state.currentValve to valveToOpen] ?: graph.shortestPath(state.currentValve, valveToOpen)
                    .also { pathsLookup[state.currentValve to valveToOpen] = it }

            val newMinutesLeft = state.minutesLeft - shortestPath - 1

            val newState = State(
                minutesLeft = newMinutesLeft,
                openedValves = hashSetOf<Valve>().apply {
                    addAll(state.openedValves)
                    add(valveToOpen)
                },
                currentValve = valveToOpen,
                pressureReleased = state.pressureReleased + valveToOpen.flowRate * newMinutesLeft
            )

            if (newState.minutesLeft <= 0 || newState.openedValves.size == valvesToOpen.size) {
                maxPressureRelieved = maxOf(maxPressureRelieved, newState.pressureReleased)
            } else {
                states.addLast(newState)
            }
        }
    }

    return maxPressureRelieved
}

fun ValvesGraph.calculateMostPressureToReleaseWithElephantHelp(): Int {
    val graph = this

    data class State(
        val minutesLeft: Int,
        val openedValves: HashSet<Valve>,
        val currentValve: Valve,
        val pressureReleased: Int,
    )

    val pathsLookup: MutableMap<Pair<Valve, Valve>, Int> = mutableMapOf()

    val maxPressureMap = hashMapOf<HashSet<Valve>, Int>()

    val startingValve = keys.first { it.name == "AA" }
    val valvesToOpen = keys.filter { it.flowRate > 0 }

    val states = ArrayDeque<State>().also {
        it.add(
            State(
                minutesLeft = 26,
                openedValves = hashSetOf(),
                currentValve = startingValve,
                pressureReleased = 0
            )
        )
    }

    while (states.isNotEmpty()) {
        val state = states.removeFirst()

        maxPressureMap[state.openedValves] =
            maxOf(maxPressureMap[state.openedValves] ?: 0, state.pressureReleased)
        
        for (valveToOpen in valvesToOpen - state.openedValves) {
            val shortestPath =
                pathsLookup[state.currentValve to valveToOpen] ?: graph.shortestPath(state.currentValve, valveToOpen)
                    .also { pathsLookup[state.currentValve to valveToOpen] = it }

            val newMinutesLeft = state.minutesLeft - shortestPath - 1

            val newState = State(
                minutesLeft = newMinutesLeft,
                openedValves = hashSetOf<Valve>().apply {
                    addAll(state.openedValves)
                    add(valveToOpen)
                },
                currentValve = valveToOpen,
                pressureReleased = state.pressureReleased + valveToOpen.flowRate * newMinutesLeft
            )

            if(newState.minutesLeft > 0 && newState.openedValves.size != valvesToOpen.size) {
                states.addLast(newState)
            }
        }
    }

    return maxPressureMap
        .toList()
        .combinations(2)
        .filter {
            val (me, elephant) = it
            me.first.intersect(elephant.first).isEmpty()
        }.maxOf {
            val (me, elephant) = it
            me.second + elephant.second
        }
}

private fun String.parseValve(): Valve = Valve(
    name = getValveName(),
    flowRate = getFlowRate(),
)

private fun String.getValveName(): String = drop(6).take(2)

private fun String.getFlowRate(): Int = split(";").first().split("=").last().toInt()

private fun String.getRoutes(): List<String> =
    split(",").map { it.takeLast(2) }

data class Valve(
    val name: String,
    val flowRate: Int,
)

fun <T> List<T>.combinations(combinationSize: Int = 2): List<List<T>> =
    map { value ->
        this.minus(value).map {
            buildList<T> {
                add(value)
                add(it)
            }
        }
    }
        .flatten()
