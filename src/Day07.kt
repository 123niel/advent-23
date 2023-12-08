fun main() {
    Day07().main()
}

class Day07 : Solution("Day07", "Day07_test", (6440 to 5905)) {
    enum class Type {
        HIGH_CARD,
        PAIR,
        TWO_PAIRS,
        THREE_OF_KIND,
        FULL_HOUSE,
        FOUR_OF_KIND,
        FIVE_OF_KIND
    }

    private fun String.type(): Type {
        val counts = groupingBy { it }.eachCount().values
        return if (5 in counts) {
            Type.FIVE_OF_KIND
        } else if (4 in counts) {
            Type.FOUR_OF_KIND
        } else if (2 in counts && 3 in counts) {
            Type.FULL_HOUSE
        } else if (3 in counts) {
            Type.THREE_OF_KIND
        } else if (counts.filter { it == 2 }.size == 2) {
            Type.TWO_PAIRS
        } else if (2 in counts) {
            Type.PAIR
        } else {
            Type.HIGH_CARD
        }
    }

    data class Play(val hand: String, val bid: Int)

    override fun part1(input: List<String>): Number {

        // encode each card to a hex-digit, so we can just sort the strings
        fun String.encode(): String {
            //              01234556789AB
            val cardRank = "23456789TJQKA"
            return map { card -> cardRank.indexOf(card).toString(16) }.joinToString("")
        }

        return input.map {
            val hand = it.substring(0, 5)
            val bid = it.substring(6).trim().toInt()
            Play(hand, bid)
        }
                .sortedWith(compareBy({ (hand, _) -> hand.type() }, { (hand, _) -> hand.encode() }))
                .mapIndexed { index, (_, bid) -> (index + 1) * bid }
                .sum()
    }

    override fun part2(input: List<String>): Number {

        fun String.typeWithJokers(): Type {
            val upgrades = this.count { it == 'J' }
            val baseType = this.filter { it != 'J' }.type()

            return (1..upgrades).fold(baseType) { type, _ ->
                when (type) {
                    Type.FIVE_OF_KIND -> Type.FIVE_OF_KIND
                    Type.FOUR_OF_KIND -> Type.FIVE_OF_KIND
                    Type.FULL_HOUSE -> Type.FOUR_OF_KIND
                    Type.THREE_OF_KIND -> Type.FOUR_OF_KIND
                    Type.TWO_PAIRS -> Type.FULL_HOUSE
                    Type.PAIR -> Type.THREE_OF_KIND
                    Type.HIGH_CARD -> Type.PAIR
                }
            }
        }

        fun String.encode(): String {
            val cardRank = "J23456789TQKA"
            return map { card -> cardRank.indexOf(card).toString(16) }.joinToString("")
        }

        return input.map {
            val hand = it.substring(0, 5)
            val bid = it.substring(6).trim().toInt()
            Play(hand, bid)
        }
                .sortedWith(compareBy({ (hand, _) -> hand.typeWithJokers() }, { (hand, _) -> hand.encode() }))
                .mapIndexed { index, (_, bid) -> (index + 1) * bid }
                .sum()
    }
}
