enum class Color(val text: String) {
    RED("red"),
    GREEN("green"),
    BLUE("blue")
}

data class Draw(val red: Int, val green: Int, val blue: Int)
data class Game(val id: Int, val draws: List<Draw>)

fun main() = Day02().main()

class Day02 : Solution("Day02", "Day02_test", (8 to 2286)) {
    override fun part1(input: List<String>): Int {
        return input.parseGames().filter { game -> game.draws.all { it.isPossible() } }
                .sumOf { it.id }
    }

    override fun part2(input: List<String>): Int {
        return input.parseGames().sumOf { game ->
            game.max { it.red } * game.max { it.green } * game.max { it.blue }
        }
    }

    private fun Map<Color, Int>.toDraw(): Draw {
        return Draw(
                get(Color.RED) ?: 0,
                get(Color.GREEN) ?: 0,
                get(Color.BLUE) ?: 0
        )
    }

    private fun List<String>.parseGames(): List<Game> {
        return map { line ->
            val colIndex = line.indexOf(":")
            val gameId = line.substring(5, colIndex).toInt()

            val draws = line.substring(colIndex + 2).let { drawsText ->
                drawsText.split("; ").map { drawText ->
                    drawText.split(", ").associate { colorCount ->
                        val (count, color) = colorCount.split(" ")
                        Color.entries.first { it.text == color } to count.toInt()
                    }.toDraw()
                }
            }

            Game(gameId, draws)
        }
    }

    private fun Game.max(color: (Draw) -> Int) =
            draws.maxOf(color)

    private fun Draw.isPossible() =
            red <= 12 && green <= 13 && blue <= 14
}