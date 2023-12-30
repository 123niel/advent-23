fun main() {
    Day16.main()
}

object Day16 : Solution() {

    private fun List<String>.parse() = map { line -> line.toCharArray() }
    private fun solve(input: List<String>, startPos: Vec, startDir: Direction): Int {
        val queue = ArrayDeque<Pair<Vec, Direction>>().apply {
            add(startPos to startDir)
        }

        val visited = mutableSetOf<Pair<Vec, Direction>>()

        while (queue.isNotEmpty()) {
            val (pos, dir) = queue.removeFirst()
            if (!(pos isIn input) || (pos to dir) in visited) {
                continue
            }
            visited.add(pos to dir)

            val symbol = input[pos]
            when (symbol) {
                '.' -> queue.add(pos.neighbour(dir) to dir)
                '-' -> {
                    if (dir == Direction.E || dir == Direction.W) {
                        queue.add(pos.neighbour(dir) to dir)
                    } else {
                        queue.add(pos.neighbour(Direction.W) to Direction.W)
                        queue.add(pos.neighbour(Direction.E) to Direction.E)
                    }
                }

                '|' -> {
                    if (dir == Direction.N || dir == Direction.S) {
                        queue.add(pos.neighbour(dir) to dir)
                    } else {
                        queue.add(pos.neighbour(Direction.S) to Direction.S)
                        queue.add(pos.neighbour(Direction.N) to Direction.N)
                    }
                }

                '\\' -> {
                    when (dir) {
                        Direction.N -> queue.add(pos.neighbour(Direction.W) to Direction.W)
                        Direction.E -> queue.add(pos.neighbour(Direction.S) to Direction.S)
                        Direction.S -> queue.add(pos.neighbour(Direction.E) to Direction.E)
                        Direction.W -> queue.add(pos.neighbour(Direction.N) to Direction.N)
                    }
                }

                '/' -> {
                    when (dir) {
                        Direction.N -> queue.add(pos.neighbour(Direction.E) to Direction.E)
                        Direction.E -> queue.add(pos.neighbour(Direction.N) to Direction.N)
                        Direction.S -> queue.add(pos.neighbour(Direction.W) to Direction.W)
                        Direction.W -> queue.add(pos.neighbour(Direction.S) to Direction.S)
                    }
                }

            }
        }


        return visited.map { it.first }.toSet().size
    }

    override fun part1(input: List<String>): Number {
        return solve(input, Vec(0, 0), Direction.E)
    }

    override fun part2(input: List<String>): Number {
        return listOf(
                input.indices.map { y -> Vec(0, y) to Direction.E },
                input.indices.map { y -> Vec(input.first().lastIndex, y) to Direction.W },
                input.first().indices.map { x -> Vec(x, 0) to Direction.S },
                input.first().indices.map { x -> Vec(x, input.lastIndex) to Direction.N }
        ).flatten().maxOf { (pos, dir) -> solve(input, pos, dir) }
    }


    override fun main() {
        readInput("Day16_test").let { input ->
            checkWithTimer("Test Input P1", 46) {
                part1(input)
            }
            checkWithTimer("Test Input P2", 51) {
                part2(input)
            }
        }

        readInput("Day16").let { input ->
            checkWithTimer("Input P1", 7860) {
                part1(input)
            }
            checkWithTimer("Input P2", 8331) {
                part2(input)
            }
        }

    }
}