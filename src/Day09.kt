fun main() {
     Day09().main()
   // Day09().part2(readInput("Day09_test")).println()
}

class Day09 : Solution("Day09", "Day09_test", (114 to 2)) {

    private fun List<String>.parse(): List<List<Int>> {
        return map { line -> line.split(" ").map { it.toInt() } }
    }

    private fun List<Int>.differences(): List<Int> {
        return (1..<size).map { get(it) - get(it - 1) }
    }

    private fun List<Int>.extrapolate(): Int {
        val diffs = differences()
        return if (diffs.all { it == 0 }) {
            last()
        } else {
            last() + diffs.extrapolate()
        }
    }
    
    private fun List<Int>.extrapolateFront(): Int {
        val diffs = differences()
        return if (diffs.all { it == 0 }) {
            first()
        } else {
            first() - diffs.extrapolateFront()
        }
    }
    override fun part1(input: List<String>): Number {

        return input.parse()
                .sumOf { history ->
                    history.extrapolate()
                }

    }

    override fun part2(input: List<String>): Number {
             return input.parse()
            .sumOf { history ->
                history.extrapolateFront()
            }
    }
}
