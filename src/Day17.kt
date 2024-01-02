import java.util.*

fun main() {
    Day17.main()
}


object Day17 : Solution() {

    private fun List<String>.parse() = map { row -> row.map { it.digitToInt() }.toIntArray() }.toTypedArray()

    data class State(val loc: Vec, val direction: Direction?, val steps: Int)

    private fun solve(
        grid: Array<IntArray>,
        start: Vec,
        end: Vec,
        validMoves: State.() -> List<State>,
        endCondition: State.() -> Boolean
    ): Int {
        val queue = PriorityQueue<Pair<State, Int>>(compareBy { a -> a.second })
        val seen = mutableSetOf<State>()

        State(start, null, 0).apply {
            queue += this to 0
            seen += this
        }

        while (queue.isNotEmpty()) {
            val (state, cost) = queue.poll()

            if (state.loc == end && state.endCondition()) {
                return cost
            }


            state.validMoves()
                .filter { next -> next !in seen }
                .forEach { nextState ->
                    queue += nextState to cost + grid[nextState.loc]
                    seen += nextState
                }

            seen.add(state)
        }

        error("Path not found")
    }

    override fun part1(input: List<String>): Number {
        val grid = input.parse()
        val start = Vec(0, 0)
        val end = Vec(input.first().lastIndex, input.lastIndex)

        val cost = solve(grid, start, end, validMoves = {

            val exclude = if (this.steps >= 3) listOfNotNull(this.direction, this.direction?.flip())
            else listOfNotNull(this.direction?.flip())


            this.loc.neighboursMap()
                .filter { (dir, loc) -> dir !in exclude && loc isIn grid }

                .map { (dir, vec) ->
                    State(
                        loc = vec,
                        direction = dir,
                        steps = if (this.direction == dir) this.steps + 1 else 1
                    )
                }
        },
            endCondition = { true })

        return cost
    }

    override fun part2(input: List<String>): Number {
        val grid = input.parse()
        val start = Vec(0, 0)
        val end = Vec(input.first().lastIndex, input.lastIndex)

        val cost = solve(grid, start, end,
            validMoves = {

                val possibleDirections = buildList {
                    if (direction != null) {
                        if (steps >= 4) {
                            add(direction.left())
                            add(direction.right())
                        }

                        if (steps < 10) {
                            add(direction)
                        }
                    } else {
                        Direction.entries.forEach { add(it) }
                    }
                }


                this.loc.neighboursMap()
                    .filter { (dir, loc) -> dir in possibleDirections && loc isIn grid }

                    .map { (dir, vec) ->
                        State(
                            loc = vec,
                            direction = dir,
                            steps = if (this.direction == dir) this.steps + 1 else 1
                        )
                    }
            },
            endCondition = {steps >= 4})

        return cost
    }


    override fun main() {
        readInput("Day17_test").let { input ->
            checkWithTimer("Test Input P1", 102) {
                part1(input)
            }
            checkWithTimer("Test Input P2", 94) {
                part2(input)
            }
        }

        readInput("Day17").let { input ->
            checkWithTimer("Input P1", 861) {
                part1(input)
            }
            checkWithTimer("Input P2", 1037) {
                part2(input)
            }
        }

    }
}