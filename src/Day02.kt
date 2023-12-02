fun main() {

    fun List<String>.toGames(): List<Pair<Int, Map<String, MutableList<Int>>>> {
        return map { line ->

            val colIndex = line.indexOf(":")
            val gameId = line.substring(5, colIndex).toInt()

            val draws = line.substring(colIndex + 2).let { game ->
                val counts = mapOf<String, MutableList<Int>>(
                    "green" to mutableListOf(),
                    "red" to mutableListOf(),
                    "blue" to mutableListOf()
                )

                game.split("; ").map {
                    buildMap {
                        it.split(", ").forEach { count ->
                            val (amount, color) = count.split(" ")
                            set(color, amount.toInt())
                        }
                    }
                }.forEach { draw ->
                    draw.forEach { (color, count) ->
                        counts[color]?.add(count)
                    }
                }

                counts
            }

            return@map (gameId to draws)
        }
    }

    fun part1(input: List<String>): Int {
        return input.toGames().map { (gameId, draws) ->
            (gameId to draws.map { (color, counts) ->
                (color to counts.max())
            }.toMap())
        }.filter { (_, counts) ->
            counts["red"]!! <= 12
                    && counts["green"]!! <= 13
                    && counts["blue"]!! <= 14
        }.sumOf { (id, _) -> id }
    }


    fun part2(input: List<String>): Int {
        return input.toGames().map { (_, draws) ->
            draws.map { (_, counts) -> counts.max() }
        }.sumOf { it.reduce { a, b -> a * b } }
    }

    readInput("Day02_test").let {
        part1(it).println()
        part2(it).println()
    }

    readInput("Day02").let {
        part1(it).println()
        part2(it).println()
    }
}


