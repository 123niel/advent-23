fun main() {
    Day08().main()
}

class Day08 : Solution("Day08", null, (2 to 6)) {

    private fun List<String>.parse(): Pair<String, Map<String, Pair<String, String>>> {
        val instructions = first()
        val pattern = Regex("""\w{3}""")

        return instructions to drop(2).associate { line ->
            val (node, left, right) = pattern.findAll(line)
                    .map { res -> res.groupValues.first() }.toList()

            node to (left to right)
        }
    }

    private fun solve(start: String,
                      nodes: Map<String, Pair<String, String>>,
                      directions: String,
                      stopCondition: (String) -> Boolean): Int {
        var i = 0
        var node = start

        while (!stopCondition(node)) {
            node = if (directions[i % directions.length] == 'L') {
                nodes[node]?.first ?: error("sould not happen")
            } else {
                nodes[node]?.second ?: error("sould not happen")
            }
            i++
        }
        return i
    }

    override fun part1(input: List<String>): Number {
        val (directions, nodes) = input.parse()

        return solve(start = "AAA",
                nodes = nodes,
                directions = directions,
                stopCondition = { it == "ZZZ" })
    }

    private fun findLCM(a: Long, b: Long): Long {
        val larger = if (a > b) a else b
        val maxLcm = a * b
        var lcm = larger
        while (lcm <= maxLcm) {
            if (lcm % a == 0L && lcm % b == 0L) {
                return lcm
            }
            lcm += larger
        }
        return maxLcm
    }

    override fun part2(input: List<String>): Number {
        val (instructions, nodes) = input.parse()

        return nodes.keys.filter { it.endsWith("A") }.map { node ->
            solve(start = node,
                    nodes = nodes,
                    directions = instructions,
                    stopCondition = { it.endsWith("Z") }).toLong()
        }.reduceRight(::findLCM)
    }
}
