fun main() {
    Day18.main()
}


object Day18 : Solution() {


    private fun List<String>.parse() = map { row ->
        row.split(' ')
            .let { (d, n, c) ->
                Triple(
                    when (d) {
                        "R" -> Direction.E
                        "L" -> Direction.W
                        "U" -> Direction.N
                        "D" -> Direction.S
                        else -> error("unknown direction")
                    }, n.toInt() ,c
                )
            }
    }

    data class State(val dug: Set<Vec>, val position: Vec)

    private fun List<Pair<Direction, Int>>.digEdge(): List<Vec> {
        return buildList {
            add(Vec(0, 0))
            this@digEdge.forEach { (dir, len) ->
                add(last().neighbour(dir, len))
            }
        }
    }

    private fun Collection<Vec>.bounds(): Pair<LongRange, LongRange> {
        val minX = minOf { vec -> vec.x }
        val maxX = maxOf { vec -> vec.x }
        val minY = minOf { vec -> vec.y }
        val maxY = maxOf { vec -> vec.y }

        return (minX..maxX) to (minY..maxY)
    }

    private fun lavaArea(edges: List<Vec>): Long {
        val area = edges.zipWithNext()
            .sumOf { (first, second) -> (first.x * second.y) - (first.y * second.x) } / 2

        val perimeter = edges.zipWithNext().sumOf { (a,b) -> a.distance(b) }

        return area +(perimeter/2)+1
    }

    override fun part1(input: List<String>): Number {
        val edges = input.parse().map { (dir, len, _) -> dir to len }.digEdge()


        return lavaArea(edges)
    }

    override fun part2(input: List<String>): Number {
        val edges = input.parse()
            .map { (_, _, c) ->
                when(c[c.lastIndex-1]) {
                    '0' -> Direction.E
                    '2' -> Direction.W
                    '3' -> Direction.N
                    '1' -> Direction.S
                    else -> error("unknown direction")
                } to c.substring(2, 7).toInt(16)
            }
            .digEdge()


        return lavaArea(edges)

    }


    override fun main() {
        readInput("Day18_test").let { input ->
            checkWithTimer("Test Input P1", 62) {
                part1(input)
            }
            checkWithTimer("Test Input P2", 952408144115) {
                part2(input)
            }
        }

        readInput("Day18").let { input ->
            checkWithTimer("Input P1", 36725) {
                part1(input)
            }
            checkWithTimer("Input P2", 97874103749720) {
                part2(input)
            }
        }

    }
}