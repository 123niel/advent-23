import kotlin.time.Duration
import kotlin.time.measureTimedValue

abstract class Solution(private val file: String,
                        private val testFile: String? = null,
                        private val testSolutions: Pair<Number, Number>? = null) {

    fun main() {
        if (testFile != null && testSolutions != null) {
            val input = readInput(testFile)
            "Test Input".println()

            val (value1, time1) = measureTimedValue { part1(input) }
            "Part 1: $value1 should be ${testSolutions.first} (${time1.inWholeMilliseconds}ms)".println()
            val (value2, time2) = measureTimedValue { part2(input) }
            "Part 2: $value2 should be ${testSolutions.second} (${time2.inWholeMilliseconds}ms)".println()
        }

        val input = readInput(file)
        "Input".println()
        val (value1, time1) = measureTimedValue { part1(input) }
        "Part 1: $value1 (${time1.inWholeMilliseconds}ms)".println()
        val (value2, time2) = measureTimedValue { part2(input) }
        "Part 2: $value2 (${time2.inWholeMilliseconds}ms)".println()
    }

    abstract fun part1(input: List<String>): Number
    abstract fun part2(input: List<String>): Number
}