fun main() {
    val stringIntMap = mapOf(
        "one" to 1,
        "two" to 2,
        "three" to 3,
        "four" to 4,
        "five" to 5,
        "six" to 6,
        "seven" to 7,
        "eight" to 8,
        "nine" to 9,
        "zero" to 0
    )


    fun String.wordToDigitOrNull(): Int? {
        for ((word, digit) in stringIntMap) {
            if (startsWith(word)) {
                return digit
            }
        }
        return null
    }

    fun part1(input: List<String>): Int = input.asSequence()
        .map { line ->
            buildList {
                for (ch in line) {
                    ch.digitToIntOrNull()?.let(::add)
                }
            }
        }.map { it.first() * 10 + it.last() }.sum()

    fun part2(input: List<String>): Int =
        input.asSequence().map { line ->
            buildList {
                for (i in line.indices) {
                    (line[i].digitToIntOrNull() ?: line.substring(i).wordToDigitOrNull())?.let(::add)
                }
            }
        }.map { it.first() * 10 + it.last() }.sum()

    val testInput = readInput("Day01_test")
    check(part1(testInput) == 142)
    check(part2(readInput("Day01_test2")) == 281)

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}