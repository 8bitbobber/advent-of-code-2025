package org.advent_of_code

import java.math.BigInteger


fun main() {
    fun part1(input: List<String>): BigInteger {
        val input = input.first()

        val ranges = input.split(',')
        val result: ArrayList<BigInteger> = arrayListOf()

        ranges.forEach { range ->
            val minMax = range.split('-')
            val min = minMax[0].toBigInteger()
            val max = minMax[1].toBigInteger()

            var number = min
            while (number <= max) {
                val numberAsString = number.toString()
                val length = numberAsString.length
                if (length % 2 != 0) {
                    number += BigInteger.ONE
                    return@forEach
                }

                val firstHalf = numberAsString.substring(0, (length / 2))
                val secondHalf = numberAsString.substring(length/2)
                if (firstHalf.equals(secondHalf)) {
                    println(numberAsString)
                    result.add(number)
                }

                number += BigInteger.ONE
            }
        }

        return result.sumOf({ x -> x })
    }

    fun checkRepeatingPattern(subset: String, value: String, cache: MutableMap<Pair<String,String>, Boolean>): Boolean {
        val key = Pair(subset, value)
        if (key in cache) {
            // wont really be called, because no overlapping ranges are provided in the task
            // but if overlapping ranges are used, this is a shortcut
            println("cache hit")
            return cache[key]!!
        }

        if (subset.length > value.length) {
            cache[key] = false
            return false
        }

        if (subset.equals(value)) {
            cache[key] = true
            return true
        }

        if (value.length % subset.length != 0) {
            return checkRepeatingPattern(subset + value.take(1), value.substring(1), cache)
        }

        val patternRepeats = value.length / subset.length
        var expected : String = ""
        for (i in 1..patternRepeats) {
            expected += subset
        }

        if (expected.equals(value)) {
            cache[key] = true
            return true
        } else {
            return checkRepeatingPattern(subset + value.take(1), value.substring(1), cache)
        }
    }

    fun part2(input: List<String>): BigInteger {
        val input = input.first()

        val ranges = input.split(',')
        val result: ArrayList<BigInteger> = arrayListOf()
        val cache = mutableMapOf<Pair<String, String>, Boolean>()

        ranges.forEach { range ->
            val minMax = range.split('-')
            val min = minMax[0].toBigInteger()
            val max = minMax[1].toBigInteger()

            var number = min
            while (number <= max) {
                val numberAsString = number.toString()
                val pattern = checkRepeatingPattern(numberAsString.take(1), numberAsString.substring(1), cache)
                if (pattern) {
                    result.add(number)
                }
                number += BigInteger.ONE
            }

        }

        return result.sumOf({ x -> x})
    }

    val input = readInput("Day02")
    val result1 = part1(input)
    println("result1: $result1")
    val result2 = part2(input)
    println("result2: $result2")
}
