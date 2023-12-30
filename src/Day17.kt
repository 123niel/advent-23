fun main() {
    Day17.main()
}

object Day17 : Solution() {

    data class Node(val pos: Vec,
                    val value: Int,
                    var cost: Int = 0,
                    var heuristic: Long = 0,
                    var parent: Node? = null,
                    var parentDir: Direction? = null
    )

    private fun List<String>.parse() =
            buildList {
                this@parse.forEachIndexed { y, line ->
                    line.forEachIndexed { x, c -> add(Node(Vec(x, y), Integer.valueOf(c.toString()))) }
                }
            }

    private fun List<Node>.find(x: Long, y: Long): Node {
        return find { it.pos.x == x && it.pos.y == y } ?: error("couldnt find node $x, $y")
    }

    override fun part1(input: List<String>): Number {
        val nodes = input.parse()
        val start = nodes.find { it.pos == Vec(0, 0) }!!
        val end = nodes.find { it.pos == Vec(input.first().lastIndex, input.lastIndex) }!!

        val openList = mutableListOf<Node>()
        val closedList = mutableListOf<Node>()

        fun heu(node: Node): Long {
            return 0
            //            return node.pos.distance(end.pos)
        }

        fun Node.neighboursMap(): Map<Direction, Node> {

            val exclude =
                    if (parentDir != null &&
                            parentDir == parent?.parentDir &&
                            parentDir == parent?.parent?.parentDir) parentDir
                    else null

            val neighboursMap = pos.neighboursMap()
                    .filter { (dir, _) -> dir != exclude }
                    .mapNotNull { (dir, vec) -> nodes.find { it.pos == vec }?.let { dir to it } }
                    .toMap()
            return neighboursMap
        }

        start.heuristic = heu(start)
        openList.add(start)

        while (openList.isNotEmpty()) {
            val currentNode = openList.minBy { it.cost + it.heuristic }

            if (currentNode == end) {
                break
            }

            openList.remove(currentNode)
            closedList.add(currentNode)

            for ((dir, neighbour) in currentNode.neighboursMap()) {
                if (neighbour in closedList) continue
                val tenativeCost = currentNode.cost + neighbour.value

                if (neighbour !in openList || tenativeCost < neighbour.cost) {
                    neighbour.cost = tenativeCost
                    neighbour.heuristic = heu(neighbour)
                    neighbour.parent = currentNode
                    neighbour.parentDir = dir
                }

                if (neighbour !in openList) {
                    openList.add(neighbour)
                }
            }
        }

        printPath(nodes, end)

        return end.cost
    }

    private fun printPath(nodes: List<Node>, end: Node) {
        var path = buildSet {
            var node = end
            while (node.parent != null) {
                add(node)
                node = node.parent as Node
            }
        }
        for (y in 0..nodes.maxOf { it.pos.y }) {
            println(buildString {
                for (x in 0..nodes.maxOf { it.pos.x }) {
                    val node = nodes.find(x, y)
                    if (node in path) {
                        append('.')
                    } else append(node.value)
                }
            })
        }

    }


    override fun part2(input: List<String>): Number {
        return 0
    }


    override fun main() {
        readInput("Day17_test").let { input ->
            checkWithTimer("Test Input P1", 46) {
                part1(input)
            }
            checkWithTimer("Test Input P2", 51) {
                part2(input)
            }
        }

//        readInput("Day17").let { input ->
//            checkWithTimer("Input P1", 7860) {
        //                part1(input)cost
//            }
//            checkWithTimer("Input P2", 8331) {
//                part2(input)
//            }
//        }

    }
}