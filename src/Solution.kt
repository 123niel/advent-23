abstract class Solution(private val file: String,
                        private val testFile: String? = null,
                        private val testSolutions: Pair<Int, Int>? = null) {

    fun main() {
        if (testFile != null && testSolutions != null) {
            val input = readInput(testFile)
            "Test Input".println()
            "Part 1: ${part1(input)} should be ${testSolutions.first}".println()
            "Part 1: ${part2(input)} should be ${testSolutions.second}".println()
        }

        val input = readInput(file)
        "Input".println()
        "Part 1: ${part1(input)}".println()
        "Part 1: ${part2(input)}".println()
    }

    abstract fun part1(input: List<String>): Int
    abstract fun part2(input: List<String>): Int
}