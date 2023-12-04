import kotlin.math.pow

fun main() {
    data class Scratchcard(val id: Int, val winningNumbers: List<Int>, val havingNumbers: List<Int>)


    val regex = Regex("Card +(\\d+): +((\\d+ *)+).+[|] +((\\d+ *)+)")

    fun String.splitTrimConvert(): List<Int> {
        return this.split(" ").filter { it.isNotBlank() }.map { it.trim().toInt() }
    }

    fun String.extractCard(): Scratchcard {
        regex.matchEntire(this)?.let { matchResult ->
            val (_, id, winning, _, having) = matchResult.groupValues
            return Scratchcard(id.toInt(), winning.splitTrimConvert(), having.splitTrimConvert())
        }
        error("Could not extract card from $this")
    }

    fun Scratchcard.countWinning(): Int {
        return havingNumbers.count { winningNumbers.contains(it) }
    }

    fun part1(input: List<String>): Int {
        return input.map { it.extractCard() }
                .map { it.countWinning() }
                .sumOf { 2.0.pow(it - 1).toInt() }
    }


    fun part2(input: List<String>): Int {

        val cards = input.map { it.extractCard() }
        val cardCount = cards.associate { it.id to 1 }.toMutableMap()

        for (card in cards) {
            val count = cardCount[card.id] ?: 0
            val winning = card.countWinning()

            if (winning > 0) {
                for (i in (card.id + 1..card.id + winning).boundTo(max = cards.size)) {
                    if (i <= cardCount.keys.size) {
                        cardCount[i] = (cardCount[i] ?: 0) + count
                    }
                }
            }
        }

        return cardCount.values.sum()
    }

    readInput("Day04_test").let {
        part1(it).println()
        part2(it).println()
    }

    readInput("Day04").let {
        part1(it).println()
        part2(it).println()
    }
}


