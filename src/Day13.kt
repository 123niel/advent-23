import kotlin.math.min

fun main() {
    Day13.main()
}

object Day13 : Solution() {

    private fun String.difference(other: String): Int {
        return indices.count { i -> this[i] != other[i] }
    }

    private fun List<String>.difference(other: List<String>): Int {
        return indices.sumOf { i -> this[i].difference(other[i]) }
    }

    private fun List<String>.findHorizontalMirror(eqFun: List<String>.(List<String>) -> Boolean): Int? {
        for (i in 0..<lastIndex) {
            val count = min(i + 1, size - i - 1)

            val first = subList(i - count + 1, i + 1)
            val second = subList(i + 1, i + 1 + count).reversed()

            if (first.eqFun(second)) {
                return i + 1
            }
        }
        return null;
    }

    override fun part1(input: List<String>): Number {
        return input.parse()
                .sumOf { block ->
                    block.findHorizontalMirror(List<String>::equals)?.let { it * 100 }
                            ?: block.transpose().findHorizontalMirror(List<String>::equals)
                            ?: error("no mirror found")
                }
    }

    override fun part2(input: List<String>): Number {
        return input.parse()
                .sumOf { block ->
                    block.findHorizontalMirror { difference(it) == 1 }?.let { it * 100 }
                            ?: block.transpose().findHorizontalMirror { difference(it) == 1 }
                            ?: error("no mirror found")
                }
    }

    override fun main() {

        readInput("Day13_test").let { input ->
            checkWithTimer("Test Input P1", 405) {
                part1(input)
            }

            checkWithTimer("Test Input P2", 400) {
                part2(input)
            }
        }
        println("")
        readInput("Day13").let { input ->
            checkWithTimer("Main Input p1", 33735) {
                part1(input)

            }
            checkWithTimer("Main Input P2", 38063) {
                part2(input)
            }

        }
    }

    private fun List<String>.parse(): List<List<String>> {
        return joinToString("\n").split("\n\n").map { it.split('\n') }
    }
}