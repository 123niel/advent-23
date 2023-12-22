fun main() {
    Day14.main()
}

object Day14 : Solution() {

    private fun List<String>.parse() = map { it.toCharArray() }

    private fun List<CharArray>.tiltNS(north: Boolean = true) {
        val yRange = if (north) indices else indices.reversed()
        val xRange = first().indices

        for (x in xRange) {
            val next = ArrayDeque<Int>()
            for (y in yRange) {
                when (this@tiltNS[y][x]) {
                    '.' -> {
                        this[y][x] = '.'
                        next.addLast(y)
                    }

                    '#' -> {
                        this[y][x] = '#'
                        next.clear()
                    }

                    'O' -> {
                        if (next.isNotEmpty()) {
                            this[next.removeFirst()][x] = 'O'
                            this[y][x] = '.'
                            next.addLast(y)
                        } else {
                            this[y][x] = 'O'
                        }
                    }
                }

            }
        }
    }

    private fun List<CharArray>.tiltWE(west: Boolean = true) {
        val yRange = indices
        val xRange = if (west) first().indices else first().indices.reversed()

        for (y in yRange) {
            val next = ArrayDeque<Int>()
            for (x in xRange) {
                when (this@tiltWE[y][x]) {
                    '.' -> {
                        this[y][x] = '.'
                        next.addLast(x)
                    }

                    '#' -> {
                        this[y][x] = '#'
                        next.clear()
                    }

                    'O' -> {
                        if (next.isNotEmpty()) {
                            this[y][next.removeFirst()] = 'O'
                            this[y][x] = '.'
                            next.addLast(x)
                        } else {
                            this[y][x] = 'O'
                        }
                    }
                }
            }
        }
    }

    private fun List<CharArray>.load(): Number {
        val yRange = indices
        val xRange = first().indices

        return xRange.sumOf { x ->
            yRange.sumOf { y ->
                if (this[y][x] == 'O') yRange.last - y + 1 else 0

            }
        }
    }


    override fun part1(input: List<String>): Number {


        val field = input.parse()
        field.tiltNS()

        return field.load()
    }

    private fun List<CharArray>.copy() = map { it.joinToString("") }
    override fun part2(input: List<String>): Number {
        fun List<CharArray>.cycle() {
            tiltNS()
            tiltWE()
            tiltNS(false)
            tiltWE(false)
        }


        val field = input.parse()
        val cache: MutableMap<List<String>, Int> = mutableMapOf(field.copy() to 0)
        val n = 1_000_000_000
        for (i in 1..n) {
            field.cycle()

            val j = cache.getOrPut(field.copy()) { i }

            if (j != i) {
                val x = j + (n - i) % (i - j)
                return cache.map { it.key }[x].map { it.toCharArray() }.load()
            }
        }

        return field.load()

    }

    override fun main() {

        readInput("Day14_test").let { input ->
            checkWithTimer("Test Input P1", 136) {
                part1(input)
            }

            checkWithTimer("Test Input P2", 64) {
                part2(input)
            }
        }
        println("")
        readInput("Day14").let { input ->
            checkWithTimer("Main Input p1", 113456) {
                part1(input)

            }
            checkWithTimer("Main Input P2", 38063) {
                part2(input)
            }

        }
    }
}

