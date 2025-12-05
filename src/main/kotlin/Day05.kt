package org.advent_of_code


fun main() {
    fun part1(input: List<String>): Int {
        val joinedInput = input.joinToString("\n")
        val parts = joinedInput.split("\n\n")

        val freshIngredientIdRanges = parts[0].lines()
            .map { line ->
                val (start, end) = line
                    .split("-")
                    .map { it.toLong() }
                start..end
            }
        val allIngredientIds = parts[1].lines()
            .map { it.toLong() }

        return allIngredientIds.count { id ->
            freshIngredientIdRanges.any { range -> id in range }
        }
    }

    fun part2(input: List<String>): Long {
        val joinedInput = input.joinToString("\n")
        val parts = joinedInput.split("\n\n")

        val freshUnmergedIngredientIdRanges = parts[0].lines()
            .map { line ->
                val (start, end) = line.split("-").map { it.toLong() }
                start to end
            }
            .sortedBy { it.first }

        val mergedRanges = mutableListOf<Pair<Long, Long>>()
        for ((start, end) in freshUnmergedIngredientIdRanges) {
            if (mergedRanges.isNotEmpty() && mergedRanges.last().second >= start - 1) {
                val lastRange = mergedRanges.removeLast()
                mergedRanges.add(lastRange.first to maxOf(lastRange.second, end))
            } else {
                mergedRanges.add(start to end)
            }
        }

        return mergedRanges.sumOf { (start, end) -> end - start + 1 }
    }

    val input = readInput("Day05")
    val resultPart1 = part1(input)
    val resultPart2 = part2(input)

    println("Part 1: $resultPart1")
    println("Part 2: $resultPart2")
}
