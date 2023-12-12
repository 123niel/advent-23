import kotlin.math.abs

fun main() {
    Day11.main()
}

object Day11 : Solution("Day11", "Day11_test", (374 to 1030)) {

    override fun main() {
        readInput("Day11_test").let { input ->
            mapOf(2 to 374,
                    10 to 1030,
                    100 to 8410).forEach { (c, expected) ->
                checkWithTimer("Test Input, c = $c", expected) { solve(input, c) }
            }
        }
        
        println("")

        readInput("Day11").let { input ->
            mapOf(2 to 9723824, 1_000_000 to 731244261352).forEach { (c, expected) ->
                checkWithTimer("Main Input, c = $c", expected) { solve(input, c) }
            }
        }
    }

    private fun solve(space: List<String>, expandCoef: Int): Number {
        val emptyRows = space.emptyRows()
        val emptyCols = space.emptyCols()

        val galaxies = space.findGalaxies()

        return galaxies.map { galaxy ->
            val xDelta = emptyCols.count { x -> x < galaxy.x }
            val yDelta = emptyRows.count { y -> y < galaxy.y }

            Vec(x = galaxy.x + (expandCoef - 1) * xDelta,
                    y = galaxy.y + (expandCoef - 1) * yDelta)
        }.allPairs().sumOf { it.distance() }


    }

    private fun List<String>.findGalaxies(): List<Vec<Long>> {
        return buildList {
            this@findGalaxies.forEachIndexed { y, line ->
                line.forEachIndexed { x, c ->
                    if (c == '#') {
                        add(Vec(x.toLong(), y.toLong()))
                    }
                }
            }
        }
    }

    private fun List<String>.emptyRows(): List<Int> {
        val emptyRows = buildList {
            for ((y, line) in this@emptyRows.withIndex()) {
                if (line.all { it == '.' }) {
                    this.add(y)
                }
            }
        }
        return emptyRows
    }

    private fun List<String>.emptyCols(): List<Int> {
        return buildList {
            for (x in this@emptyCols.first().indices) {
                if (this@emptyCols.map { it[x] }.all { it == '.' }) {
                    add(x)
                }
            }
        }
    }

    private fun <T> List<T>.allPairs(): List<Pair<T, T>> =
            flatMapIndexed { i1, v1 ->
                filterIndexed { i, _ -> i > i1 }.map { v2 -> v1 to v2 }
            }

    private fun Pair<Vec<Long>, Vec<Long>>.distance(): Long =
            abs(first.x - second.x) + abs(first.y - second.y)
}
