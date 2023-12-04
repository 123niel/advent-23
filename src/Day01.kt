fun main() = Day01().main()

class Day01 : Solution("Day01", "Day01_test", (142 to 281)) {
    override fun part1(input: List<String>): Int {
        return input.asSequence()
                .map { line ->
                    buildList {
                        for (ch in line) {
                            ch.digitToIntOrNull()?.let(::add)
                        }
                    }
                }.map { it.first() * 10 + it.last() }.sum()
    }

    override fun part2(input: List<String>): Int {
        return input.asSequence().map { line ->
            buildList {
                for (i in line.indices) {
                    (line[i].digitToIntOrNull() ?: line.substring(i).wordToDigitOrNull())?.let(::add)
                }
            }
        }.map { it.first() * 10 + it.last() }.sum()
    }

    private val digitWords = listOf("one", "zero", "two", "three", "four", "five", "six", "seven", "eight", "nine")
    private fun String.wordToDigitOrNull(): Int? {
        digitWords.forEachIndexed { digit, word ->
            if (startsWith(word)) {
                return digit
            }
        }
        return null
    }
}