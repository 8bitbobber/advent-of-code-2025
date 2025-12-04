package org.advent_of_code


fun main() {
    fun part1(input: List<String>): Int {
        fun maxJoltage(bank: String): Int {
            var maxJoltage = 0
            for (bankIndex in bank.indices) {
                for (bankIndexPlusOne in bankIndex + 1 until bank.length) {
                    val joltage = "${bank[bankIndex]}${bank[bankIndexPlusOne]}".toInt()
                    if (joltage > maxJoltage) {
                        maxJoltage = joltage
                    }
                }
            }
            return maxJoltage
        }

        return input.sumOf { maxJoltage(it) }
    }

    fun buildBatteryPack(positions: List<Int>, bank: String): Long {
        val result = buildString {
            for (position in positions) {
                append(bank[position])
            }
        }

        return result.toLong()
    }

    // basically useless after battery size of 4 or larger (bruteforcing)
    fun part2IndexHell(input: List<String>): Long {
        // every line is a list of available numbers
        // build a battery of size 2, which has the greatest value
        // by tracking all positions of numbers
        // this can be bruteforced without recursion
        // and no tricks

        val size = 3

        val lastPositionIndex = size - 1

        val batteryPacks = mutableListOf<Long>()
        for (bank in input) {
            val positions = (0..lastPositionIndex).toMutableList()
            val bankLength = bank.length
            var currentPositionWorkingIndex = size - 1
            var highestBatteryPack: Long = 0
            val lastBankIndex = bank.length - 1
            while (currentPositionWorkingIndex >= 0) {// when first battery position is at the last possible position, we're done for this bank
                // build battery pack for current position
                val currentBatteryPack = buildBatteryPack(positions, bank)
                if (highestBatteryPack < currentBatteryPack) {
                    highestBatteryPack = currentBatteryPack
                }

                // if this condition is true, the final state of the current working position has been reached -> reset positions
                if (positions[currentPositionWorkingIndex] == bankLength - size + currentPositionWorkingIndex) {
                    if (currentPositionWorkingIndex == 0) {
                        break
                    }

                    // trigger a reset
                    val indexesToMove = (currentPositionWorkingIndex..lastPositionIndex)
                    val pp = positions[currentPositionWorkingIndex - 1]

                    var positionMover = 1
                    for (indexToMove in indexesToMove) {
                        positions[indexToMove] = pp + positionMover
                        positionMover++
                    }
                    // positions are now reset
                    currentPositionWorkingIndex--
                }

                // move normally the next expected indexes
                val indexesToCheck = (currentPositionWorkingIndex..lastPositionIndex).reversed()
                for (index in indexesToCheck) {
                    // this position index, reached a point where it cannot move further
                    if (positions[index] == bankLength - size + index) {
                        // check if you can move back with all children and take parent and move together 1 further
                        // if you can move back, go back to parent+1, then move
                        // if you're stuck ignore
                        var parentPosition = positions[index - 1]
                        if (positions[index] - parentPosition > 1) {
                            // you're not stuck, take all your children and move to parent
                            // and then move with parent and children + 1
                            val indexesToMove = (index  ..lastPositionIndex)
                            parentPosition = positions[index - 1] + 1
                            positions[index - 1] = parentPosition
                            var moveCounter = 1
                            for (indexToMove in indexesToMove) {
                                positions[indexToMove] = parentPosition + moveCounter
                                moveCounter++
                            }

                            break
                        }
                        else {
                            // if you cant move
                            // ignore, because parent will also be stuck -> if parent is stuck does the same logic with its parents.
                        }
                    } else {
                        // position is not at end position, move one further
                        positions[index] = positions[index] + 1

                        break
                    }
                }
            }

            batteryPacks.add(highestBatteryPack)
        }

        return batteryPacks.sum()
    }

    fun part2optimized(input: List<String>): Long {
        fun maxJoltage(bank: String): Long {
            val bankLength = bank.length
            val batteryPacksize = 12

            if (bankLength < batteryPacksize) {
                return 0
            }

            val stack = mutableListOf<Char>()
            val toRemove = bankLength - batteryPacksize
            var remainingRemovals = toRemove

            for (digit in bank) {
                while (stack.isNotEmpty() && remainingRemovals > 0 && stack.last() < digit) {
                    stack.removeAt(stack.size - 1)
                    remainingRemovals--
                }
                stack.add(digit)
            }

            while (stack.size > batteryPacksize) {
                stack.removeAt(stack.size - 1)
            }

            return stack.joinToString("").toLong()
        }

        return input.sumOf { maxJoltage(it) }
    }

    val input = readInput("Day03")
    var opt = part2optimized(input)
    var slow = part2IndexHell(input)

    println(opt)
    println(slow)

}
