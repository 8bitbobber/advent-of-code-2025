package org.advent_of_code

import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt


fun main() {

    fun part1(input: List<String>): Int {
        val junctions = input.map { it.toPosition() }
        val junctionDistances: MutableMap<Pair<JunctionPosition, JunctionPosition>, Long> = mutableMapOf()
        // calculate distance between all points to eachother
        // then order it ascending
        junctions.forEach { junction ->
            junctions.forEach { junctionPartner ->
                val pair = if (junction smallerThan junctionPartner) Pair(junction, junctionPartner) else Pair(junctionPartner, junction)
                if (!junctionDistances.containsKey(pair)) {
                    val distance = junction.distanceTo(junctionPartner)
                    if (distance > 0) {
                        junctionDistances[pair] = distance
                    }
                }
            }
        }

        val junctionLookup: MutableMap<JunctionPosition, MutableList<JunctionPosition>> = mutableMapOf()
        val distancesSorted = junctionDistances.toList().sortedBy { (_, value) -> value }.take(1000)
        for (pair in distancesSorted) {
            val firstIsPartOfACircuit = junctionLookup.containsKey(pair.first.first)
            val secondIsPartOfACircuit = junctionLookup.containsKey(pair.first.second)
            if (firstIsPartOfACircuit && secondIsPartOfACircuit) {
                val firstCircuit = junctionLookup[pair.first.first]!!
                val secondCircuit = junctionLookup[pair.first.second]!!

                // if both are part of the same network -> skip
                if (firstCircuit == secondCircuit) {
                    continue;
                }

                // merging two circuits
                firstCircuit.addAll(secondCircuit)
                junctionLookup[pair.first.first] = firstCircuit
                junctionLookup[pair.first.second] = firstCircuit
                for (junction in secondCircuit) {
                    junctionLookup[junction] = firstCircuit
                }
                continue
            }

            if (!firstIsPartOfACircuit && !secondIsPartOfACircuit) {
                val circuit = mutableListOf(pair.first.first, pair.first.second)
                junctionLookup[pair.first.first] = circuit
                junctionLookup[pair.first.second] = circuit
            }

            if (firstIsPartOfACircuit) {
                val circuit = junctionLookup[pair.first.first]!!
                circuit.add(pair.first.second)
                junctionLookup[pair.first.second] = circuit
            }

            if (secondIsPartOfACircuit) {
                val circuit = junctionLookup[pair.first.second]!!
                circuit.add(pair.first.first)
                junctionLookup[pair.first.first] = circuit
            }
        }

        val result = junctionLookup
            .map { it.value }
            .distinct()
            .map{ it.size }
            .sortedByDescending { it }
            .take(3)
            .reduce { acc, i -> acc * i }
        return result
    }

    fun part2(input: List<String>): Long {
        val junctions = input.map { it.toPosition() }
        val junctionDistances: MutableMap<Pair<JunctionPosition, JunctionPosition>, Long> = mutableMapOf()
        // calculate distance between all points to eachother
        // then order it ascending
        junctions.forEach { junction ->
            junctions.forEach { junctionPartner ->
                val pair = if (junction smallerThan junctionPartner) Pair(junction, junctionPartner) else Pair(junctionPartner, junction)
                if (!junctionDistances.containsKey(pair)) {
                    val distance = junction.distanceTo(junctionPartner)
                    if (distance > 0) {
                        junctionDistances[pair] = distance
                    }
                }
            }
        }

        var lastPairThatActuallyMadeAConnection: Pair<JunctionPosition, JunctionPosition>? = null
        val junctionLookup: MutableMap<JunctionPosition, MutableList<JunctionPosition>> = mutableMapOf()
        val distancesSorted = junctionDistances.toList().sortedBy { (_, value) -> value }
        for (pair in distancesSorted) {
            val firstIsPartOfACircuit = junctionLookup.containsKey(pair.first.first)
            val secondIsPartOfACircuit = junctionLookup.containsKey(pair.first.second)
            if (firstIsPartOfACircuit && secondIsPartOfACircuit) {
                val firstCircuit = junctionLookup[pair.first.first]!!
                val secondCircuit = junctionLookup[pair.first.second]!!

                // if both are part of the same network -> skip
                if (firstCircuit == secondCircuit) {
                    continue;
                }

                // merging two circuits
                firstCircuit.addAll(secondCircuit)
                junctionLookup[pair.first.first] = firstCircuit
                junctionLookup[pair.first.second] = firstCircuit
                for (junction in secondCircuit) {
                    junctionLookup[junction] = firstCircuit
                }
                lastPairThatActuallyMadeAConnection = pair.first
                continue
            }

            if (!firstIsPartOfACircuit && !secondIsPartOfACircuit) {
                val circuit = mutableListOf(pair.first.first, pair.first.second)
                junctionLookup[pair.first.first] = circuit
                junctionLookup[pair.first.second] = circuit
            }

            if (firstIsPartOfACircuit) {
                val circuit = junctionLookup[pair.first.first]!!
                circuit.add(pair.first.second)
                junctionLookup[pair.first.second] = circuit
                lastPairThatActuallyMadeAConnection = pair.first
            }

            if (secondIsPartOfACircuit) {
                val circuit = junctionLookup[pair.first.second]!!
                circuit.add(pair.first.first)
                junctionLookup[pair.first.first] = circuit
                lastPairThatActuallyMadeAConnection = pair.first
            }
        }

        return lastPairThatActuallyMadeAConnection!!.first.x * lastPairThatActuallyMadeAConnection.second.x
    }

    val input = readInput("Day08")
    val resultPart1 = part1(input)
    val resultPart2 = part2(input)

    println("Part 1: $resultPart1")
    println("Part 2: $resultPart2")
}

data class JunctionPosition(val x: Long, val y: Long, val z: Long)

fun String.toPosition(): JunctionPosition {
    val coordinates = this.split(',').map { it.toLong() }
    return JunctionPosition(coordinates[0], coordinates[1], coordinates[2])
}

fun JunctionPosition.distanceTo(junctionPartner: JunctionPosition): Long {
    val xDiff = abs(this.x - junctionPartner.x).toDouble()
    val yDiff = abs(this.y - junctionPartner.y).toDouble()
    val zDiff = abs(this.z - junctionPartner.z).toDouble()

    return sqrt(xDiff.pow(2) + yDiff.pow(2) + zDiff.pow(2)).toLong()
}

infix fun JunctionPosition.smallerThan(junctionPartner: JunctionPosition): Boolean {
    return when {
        this.x != junctionPartner.x -> this.x < junctionPartner.x
        this.y != junctionPartner.y -> this.y < junctionPartner.y
        else -> this.z < junctionPartner.z
    }
}