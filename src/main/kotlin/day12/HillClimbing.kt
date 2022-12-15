package day12

import util.readFileLines
import kotlin.math.abs

private const val fileName = "/day12.txt"

fun solveDay121() {
    val input = readFileLines(fileName)

    val nodes = input.toNodes()
    val pathLength = calculateShortestPath(nodes = nodes, startPosition = nodes.first { it.isStart }.position)
    println(pathLength)
}

fun solveDay122() {
    val input = readFileLines(fileName)

    val nodes = input.toNodes()

    val startingPoints = nodes.filter { it.elevation == 'a' }
    val pathLength = startingPoints
        .map { startingPoint ->
            calculateShortestPath(
                nodes = input.toNodes().map { it.copy(isStart = it.position == startingPoint.position) },
                startPosition = startingPoint.position
            )
        }
        .minByOrNull { it }!!
    println(pathLength)
}

/**
 * @return number of nodes visited from start to destination
 * Using A* Search Algorithm
 */
private fun calculateShortestPath(nodes: List<Node>, startPosition: Position): Int {
    val start = nodes.first { it.position == startPosition }
    val destination = nodes.first { it.isDestination }

    val open = mutableListOf(start)
    val closed = mutableListOf<Node>()

    while (open.isNotEmpty()) {
        val next = open.minByOrNull { it.cost }!!
        open.remove(next)

        val neighbours = next.getNeighbours(nodes)
        if (neighbours.any { it.isDestination && next.canMoveTo(destination) }) {
            destination.parent = next
            break
        }

        neighbours.forEach { node ->
            val distanceFromStart = node.computeDistanceFromStart(parent = next, start = start)
            val distanceFromDestination = node.computeDistanceFromDestination(destination)

            val cost = distanceFromStart + distanceFromDestination

            val currentOpenNodeOnThisPosition = open.firstOrNull { it.position == node.position }
            val currentClosedNodeOnThisPosition = closed.firstOrNull { it.position == node.position }

            val shouldInspectNode = next.canMoveTo(node) &&
                    (currentOpenNodeOnThisPosition == null || currentOpenNodeOnThisPosition.cost >= cost) &&
                    (currentClosedNodeOnThisPosition == null || currentClosedNodeOnThisPosition.cost >= cost)

            //todo refactor so it is more readable
            if (shouldInspectNode) {
                node.distanceFromDestination = distanceFromDestination
                node.distanceFromStart = distanceFromStart
                node.parent = next
                if (currentOpenNodeOnThisPosition == null) {
                    open.add(node)
                }
            }
        }
        closed.add(next)
    }

    if (destination.parent == null) {
        return Int.MAX_VALUE // path to parent not found
    }

    var pathLength = 0
    var next = destination
    while (next.parent != null) {
        pathLength++
        next = next.parent!!
    }
    return pathLength
}

private fun Node.canMoveTo(other: Node): Boolean {
    return other.elevation - elevation <= 1
}

private fun Node.computeDistanceFromStart(parent: Node, start: Node): Int {
    return if (parent.position == start.position) 1 else parent.distanceFromStart + 1
}

private fun Node.computeDistanceFromDestination(destination: Node): Int {
    return abs(this.position.x - destination.position.x) + abs(this.position.y - destination.position.y)
}

private fun Node.getNeighbours(nodes: List<Node>): List<Node> {
    val left = nodes.firstOrNull { it.position == Position(x = position.x - 1, y = position.y) }
    val right = nodes.firstOrNull { it.position == Position(x = position.x + 1, y = position.y) }
    val top = nodes.firstOrNull { it.position == Position(x = position.x, y = position.y - 1) }
    val bottom = nodes.firstOrNull { it.position == Position(x = position.x, y = position.y + 1) }

    return listOfNotNull(left, right, top, bottom)
}

private fun List<String>.toNodes() = this
    .mapIndexed { column, line -> line.mapIndexed { row, char -> char.parseNode(Position(row, column)) } }
    .flatten()

private data class Position(val x: Int, val y: Int)

private fun Char.parseNode(position: Position): Node {
    val elevation = when (this) {
        'S' -> 'a'
        'E' -> 'z'
        else -> this
    }

    val isStart = this == 'S'
    val isDestination = this == 'E'

    return Node(
        elevation = elevation,
        isStart = isStart,
        isDestination = isDestination,
        position = position
    )
}

private data class Node(
    val elevation: Char,
    val isDestination: Boolean,
    val isStart: Boolean,
    val position: Position,
) {
    var parent: Node? = null
    var distanceFromStart = Int.MAX_VALUE / 2
    var distanceFromDestination = Int.MAX_VALUE / 2

    val cost: Int
        get() = if (isStart) 0 else distanceFromStart + distanceFromDestination
}