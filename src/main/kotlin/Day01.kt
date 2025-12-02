package org.advent_of_code


fun main() {
    fun part1(input: List<String>): Int {
        var currentPosition: Int = 50
        var zeroCounter: Int = 0

        input.forEach { x ->
            val direction = x.substring(0, 1)
            val value = x.substring(1).toInt()
            if (direction.equals("R")) {
                currentPosition = (currentPosition + value) % 100
            } else {
                currentPosition = (currentPosition - value) % 100
            }

            if (currentPosition == 0) {
                zeroCounter++;
            }
        }


        return zeroCounter
    }

    fun part2(input: List<String>): Int {
        var currentPosition: Int = 50
        var zeroCounter: Int = 0

        input.forEach { x ->
            val direction = x.substring(0, 1)
            val value = x.substring(1).toInt()
            val rotations: Int = (value / 100)
            zeroCounter += rotations
            val previousPosition: Int = currentPosition
            val normalizedRotation = value - rotations * 100

            if (normalizedRotation == 0) {
                return@forEach
            }

            if (direction.equals("R")) {
                currentPosition = (currentPosition + normalizedRotation) % 100
                if ( currentPosition == 0 || currentPosition < previousPosition) {
                    zeroCounter++;
                }
            } else {
                currentPosition = (currentPosition - normalizedRotation) % 100
                if (currentPosition < 0) {
                    currentPosition += 100 // put negative position into 0..99 number space
                }
                if (currentPosition == 0 || (currentPosition > previousPosition && previousPosition != 0)) {
                    zeroCounter++;
                }
            }
        }

        return zeroCounter
    }

    val input = readInput("Day01")
    val resultPart1 = part1(input)
    val resultPart2 = part2(input)
    check(part2(listOf("L49")) == 0)
    check(part2(listOf("L50")) == 1)
    check(part2(listOf("L100")) == 1)
    check(part2(listOf("R49")) == 0)
    check(part2(listOf("R50")) == 1)
    check(part2(listOf("R500")) == 5)
    check(part2(listOf("L500", "R500")) == 10)
    check(part2(listOf("L500", "L49")) == 5)
    check(part2(listOf("L100", "R100")) == 2)
    check(part2(listOf("L100", "R150")) == 3)
    check(part2(listOf("L100", "R150", "L50")) == 3)

    println("Part 1: $resultPart1")
    println("Part 2: $resultPart2")
}
