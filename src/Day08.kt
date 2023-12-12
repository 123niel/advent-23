fun main() {
    Day08.main()
}

object Day08 : Solution {
    override fun main() {
        readInput("Day08").let { input ->
            val (directions, nodes) = input.parse()
            checkWithTimer("Part 1", 13939) {

                traverse(start = "AAA",
                        nodes = nodes,
                        directions = directions,
                        stopCondition = { it == "ZZZ" })
            }

            checkWithTimer("Part 2", 8906539031197) {
                nodes.keys.filter { it.endsWith("A") }.map { node ->
                    traverse(start = node,
                            nodes = nodes,
                            directions = directions,
                            stopCondition = { it.endsWith("Z") }).toLong()
                }.reduceRight(::findLCM)
            }
        }
    }

    private fun List<String>.parse(): Pair<String, Map<String, Pair<String, String>>> {
        val instructions = first()
        val pattern = Regex("""\w{3}""")

        return instructions to drop(2).associate { line ->
            val (node, left, right) = pattern.findAll(line)
                    .map { res -> res.groupValues.first() }.toList()

            node to (left to right)
        }
    }

    private fun traverse(start: String,
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
}
