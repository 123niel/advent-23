fun main() {
    Day09.main()
}

object Day09 : Solution("Day09", "Day09_test", (114 to 2)) {

    private fun List<String>.parse(): List<List<Int>> {
        return map { line -> line.split(" ").map { it.toInt() } }
    }

    private fun List<Int>.differences(): List<Int> {
        return zipWithNext().map { (a, b) -> b - a }
    }

    private tailrec fun List<Int>.extrapolate(acc: Int = 0): Int {
        val diffs = differences()
        return if (diffs.all { it == 0 }) {
            last() + acc
        } else {
            diffs.extrapolate(acc + last())
        }
    }

    override fun part1(input: List<String>): Number {
        return input.parse()
                .sumOf { it.extrapolate() }
    }

    override fun part2(input: List<String>): Number {
        return input.parse()
                .sumOf { it.reversed().extrapolate() }
    }
}
