import kotlin.time.measureTimedValue

abstract class Solution(private val file: String? = null,
                        private val testFile: String? = null,
                        private val testSolutions: Pair<Number, Number>? = null) {

    open fun main() {
        if (testFile != null && testSolutions != null) {
            val input = readInput(testFile)
            checkWithTimer("Test Input Part 1", testSolutions.first) { part1(input) }
            checkWithTimer("Test Input Part 2", testSolutions.second) { part2(input) }
        }

        "".println()

        file?.let {
            val input = readInput(it)
            withTimer("Main Input Part 1") { part1(input) }
            withTimer("Main Input Part 2") { part2(input) }
        }

    }

    open fun part1(input: List<String>): Number = 0
    open fun part2(input: List<String>): Number = 0

}