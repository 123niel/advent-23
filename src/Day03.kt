fun main() = Day03().main()

class Day03 : Solution("Day03", "Day03_test", (4361 to 467835)) {
    override fun part1(input: List<String>): Int {
        val counted = input.map { line -> line.map { false }.toMutableList() }
        val numbers = mutableListOf<Int>()

        input.forEach2D { x, y, char ->
            if (char.isSymbol()) {
                numbers.addAll(findNumbers(input, counted, x, y))
            }
        }
        return numbers.sum()
    }

    override fun part2(input: List<String>): Int {
        val counted = input.map { line -> line.map { false }.toMutableList() }

        val gearRatios = mutableListOf<Int>()

        input.forEach2D { x, y, char ->
            if (char == '*') {
                val numbers = findNumbers(input, counted, x, y)

                if (numbers.size == 2) {
                    gearRatios.add(numbers[0] * numbers[1])
                }
            }

        }
        return gearRatios.sum()
    }

    private fun Char.isSymbol() = !(isDigit() || equals('.'))

    private fun String.findNumberRange(index: Int): IntRange {
        val startOrNull = (index downTo 0).firstOrNull { !get(it).isDigit() }
        val stopOrNull = (index until length).firstOrNull { !get(it).isDigit() }

        val start = startOrNull?.plus(1) ?: 0
        val stop = stopOrNull?.minus(1) ?: (length - 1)

        return start..stop
    }

    private fun List<String>.forEach2D(body: (x: Int, y: Int, char: Char) -> Unit) {
        forEachIndexed { y, line ->
            line.forEachIndexed { x, char ->
                body(x, y, char)
            }
        }
    }

    operator fun List<String>.get(x: Int, y: Int) = get(y)[x]
    operator fun <T> List<List<T>>.get(x: Int, y: Int) = get(x)[y]
    operator fun <T> List<MutableList<T>>.set(x: Int, y: Int, value: T) {
        get(x)[y] = value
    }

    private fun findNumbers(input: List<String>, counted: List<MutableList<Boolean>>, x: Int, y: Int): List<Int> {
        val numbers = mutableListOf<Int>()

        val xs = (x - 1..x + 1).boundTo(0, input.first().length - 1)
        val ys = (y - 1..y + 1).boundTo(0, input.size - 1)

        for (i in xs) {
            for (j in ys) {
                if (input[i, j].isDigit() && !counted[i, j]) {
                    val range = input[j].findNumberRange(i)
                    range.forEach { counted[it, j] = true }
                    val num = input[j].substring(range).toInt()
                    numbers.add(num)
                }
            }
        }
        return numbers.toList()
    }
}
