fun main() {
    Day12.main()
}

object Day12 : Solution() {
    data class Row(val springs: String, val groupSizes: List<Int>)

    private fun Row.unfold(): Row {
        val unfoldedSprings = List(5) { springs }.joinToString("?")
        val unfoldedSizes = List(5) { groupSizes }.flatten()

        return Row(unfoldedSprings, unfoldedSizes)
    }

    private val cache = mutableMapOf<Row, Long>()
    private fun cached(row: Row, res: Long): Long {
        cache[row] = res
        return res
    }

    private fun possibillities(springs: String, sizes: List<Int>): Long {
        val row = Row(springs, sizes)
        if (cache.containsKey(row)) {
            return cache[row]!!
        }

        if (springs.isEmpty()) {
            return if (sizes.isEmpty()) {
                1
            } else 0
        }


        when (springs.first()) {
            '.' -> return cached(row, possibillities(springs.drop(1), sizes))
            '?' -> return cached(row, possibillities(springs.replaceFirstChar { '#' }, sizes) + possibillities(springs.drop(1), sizes))
        }
        if (sizes.isEmpty()) {
            return cached(row, 0)
        }

        val firstSize = sizes.first()

        // check if string is long enough for first group
        if (springs.length < sizes.first()) {
            return cached(row, 0)
        }
        // check if first n chars are not '.'
        if (springs.take(firstSize).any { it == '.' }) {
            return cached(row, 0)
        }

        val nextChar = springs.getOrNull(firstSize)

        return when (nextChar) {
            // check if next char is '#' -> 0
            '#' -> cached(row, 0)
            // check if next char is '.' or '?' or null
//            '.', '?'
            else -> cached(row, possibillities(
                    springs = springs.drop(firstSize + 1),
                    sizes = sizes.drop(1)
            ))
        }
    }

    override fun part1(input: List<String>): Long {
        return input.parse()
                .map { row -> possibillities(row.springs, row.groupSizes) }
                .sum()
    }

    override fun part2(input: List<String>): Long {
        return input.parse()
                .map { it.unfold() }
                .map { row -> possibillities(row.springs, row.groupSizes) }
                .sum()
    }

    override fun main() {
        readInput("Day12_test").let { input ->
            checkWithTimer("Test Input P1", 21) {
                part1(input)
            }

            checkWithTimer("Test Input P2", 525152) {
                part2(input)
            }

        }
        println("")
        readInput("Day12").let { input ->
            checkWithTimer("Main Input p1", 7007) {
                part1(input)

            }
            checkWithTimer("Main Input P2", 3476169006222) {
                part2(input)
            }

        }
    }


    private fun List<String>.parse() =
            asSequence()
                    .map { it ->
                        val (springs, sizes) = it.split(" ")
                        Row(springs, sizes.split(",").map { it.toInt() })
                    }

}
