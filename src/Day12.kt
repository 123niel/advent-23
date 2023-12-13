fun main() {
    Day12.main()
}

object Day12 : Solution() {

    /*
         ???.###             1,1,3
         .??..??...?##.      1,1,3
         ?#?#?#?#?#?#?#?     1,3,1,6
         ????.#...#...       4,1,1
         ????.######..#####. 1,6,5
         ?###????????       3,2,1
     */

    data class Row(val springs: String, val groupSizes: List<Int>)

    private val cache= mutableMapOf<Row, Int>()
    
    
    private fun possibillities(springs: String, sizes: List<Int>): Int {

        if (springs.isEmpty()) {
            return if (sizes.isEmpty()) {
                1
            } else 0
        }


        when (springs.first()) {
            '.' -> return possibillities(springs.drop(1), sizes)
            '?' -> return possibillities(springs.replaceFirstChar { '#' }, sizes) + possibillities(springs.drop(1), sizes)
        }
        if (sizes.isEmpty()) {
            return 0
        }

        val firstSize = sizes.first()

        // must start with #

        // check if string is long enough for first group
        if (springs.length < sizes.first()) {
            return 0
        }
        // check if first n chars are not '.'
        if (springs.take(firstSize).any { it == '.' }) {
            return 0
        }

        val nextChar = springs.getOrNull(firstSize)

        return when (nextChar) {
            // check if next char is '#' -> 0
            '#' -> 0
            // check if next char is '.' or '?' or null
//            '.', '?'
            else -> possibillities(
                    springs = springs.drop(firstSize + 1),
                    sizes = sizes.drop(1),
            )
        }
    }

    override fun main() {
        readInput("Day12_test").let { input ->
            checkWithTimer("Test Input", 21) {
                input.parse()
                        .map { row -> possibillities(row.springs, row.groupSizes) }
                        .sum()
            }

        }


        println("")

        readInput("Day12").let { input ->
            withTimer("Main Input") {
                input.parse()
                        .map { row -> possibillities(row.springs, row.groupSizes) }
                        .sum()
            }
        }

    }

    fun test() {
        mapOf(
                "???.### 1,1,3" to 1,
                ".??..??...?##. 1,1,3" to 4,
                "?#?#?#?#?#?#?#? 1,3,1,6" to 1,
                "????.#...#... 4,1,1" to 1,
                "????.######..#####. 1,6,5" to 4,
                "?###???????? 3,2,1" to 10
        ).forEach { (input, expected) ->
            checkWithTimer(input, expected) {
                val (springs, sizes) = input.parseLine()
                return@checkWithTimer possibillities(springs, sizes)
            }
        }
    }

    private fun String.parseLine(): Row {
        val (springs, sizes) = split(" ")
        return Row(springs, sizes.split(",").map { it.toInt() })

    }

    private fun List<String>.parse() =
            asSequence()
                    .map { it.parseLine() }


}
