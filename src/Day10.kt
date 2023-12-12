

fun main() {
    Day10.main()
}

object Day10 : Solution("Day10", "Day10_test", (8 to 0)) {

    enum class Direction { N, E, S, W }

    private fun Vec<Int>.north() = Vec(x, y - 1)
    private fun Vec<Int>.east() = Vec(x + 1, y)
    private fun Vec<Int>.south() = Vec(x, y + 1)
    private fun Vec<Int>.west() = Vec(x - 1, y)

    private fun Vec<Int>.walk(d: Direction, input: List<String>): Pair<Vec<Int>, Char> {
        val next = when (d) {
            Direction.E -> east()
            Direction.N -> north()
            Direction.S -> south()
            Direction.W -> west()
        }
        return next to input[next]
    }


    private fun List<String>.findStart(): Vec<Int> {
        forEachIndexed { i, line ->
            val pos = line.indexOf('S')
            if (pos != -1) {
                return Vec(pos, i)
            }
        }
        error("Couldn't find start")
    }

    private fun Vec<Int>.scanForPipes(input: List<String>): Direction {

        return if (input[north()] in "|7F") {
            Direction.N
        } else if (input[east()] in "-J7") {
            Direction.E
        } else if (input[south()] in "|JL") {
            Direction.S
        } else {
            Direction.W
        }
    }

    private fun List<String>.findPipeLoop(): List<Vec<Int>> {
        val start = findStart()
        var dir = start.scanForPipes(this)
        val list = mutableListOf(start)

        var (current, currentChar) = start.walk(dir, this)

        while (currentChar != 'S') {
            list.add(current)
            dir = when (dir) {
                Direction.N -> when (currentChar) {
                    '|' -> Direction.N
                    '7' -> Direction.W
                    'F' -> Direction.E
                    else -> error("unknown symbol")
                }

                Direction.E -> when (currentChar) {
                    '-' -> Direction.E
                    'J' -> Direction.N
                    '7' -> Direction.S
                    else -> error("unknown symbol")
                }

                Direction.S -> when (currentChar) {
                    '|' -> Direction.S
                    'J' -> Direction.W
                    'L' -> Direction.E
                    else -> error("unknown symbol")
                }

                Direction.W -> when (currentChar) {
                    '-' -> Direction.W
                    'F' -> Direction.S
                    'L' -> Direction.N
                    else -> error("unknown symbol")
                }
            }

            val (next, nextChar) = current.walk(dir, this)
            current = next
            currentChar = nextChar
        }

        return list
    }

    override fun part1(input: List<String>): Number {
        return input.findPipeLoop().size / 2
    }

    override fun part2(input: List<String>): Number {
        val loop = input.findPipeLoop()
        val chars = listOf('|', 'F', '7')
        
        var inside = false
        var count = 0
        
        for (y in input.indices) {
            for (x in input.first().indices) {
                val vec = Vec(x, y)
                val char = input[vec]
                
                if (vec in loop && char in chars) {
                    inside = !inside
                } else if(vec !in loop && inside) {
                    count++
                }
            }
        }

        return count
    }
}
