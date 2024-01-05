fun main() {
    Day19.main()
}

object Day19 : Solution() {

    data class PartRange(val x: IntRange, val m: IntRange, val a: IntRange, val s: IntRange) {
        private val map = mapOf("x" to x, "m" to m, "a" to a, "s" to s)

        constructor(x: Int, m: Int, a: Int, s: Int) : this(x..x, m..m, a..a, s..s)

        fun isEmpty() = listOf(x, m, a, s).any { it.isEmpty() }
        fun sum() = map.values.sumOf { it.first }
        fun combinations() = map.values.map { range -> range.count().toLong() }.reduce { a, b -> a * b }
        operator fun get(category: String) = map[category] ?: error("unknown category")

        fun split(category: String, predicate: (Int) -> Boolean): Pair<PartRange, PartRange> {
            val (yes, no) = this[category].split(predicate)

            val passed = map.map { (cat, range) ->
                if (cat == category) {
                    cat to yes
                } else {
                    cat to range
                }
            }.toMap()

            val denied = map.map { (cat, range) ->
                if (cat == category) {
                    cat to no
                } else {
                    cat to range
                }
            }.toMap()

            return of(passed) to of(denied)
        }

        companion object {

            val EMPTY = PartRange(IntRange.EMPTY, IntRange.EMPTY, IntRange.EMPTY, IntRange.EMPTY)
            fun of(str: String) = str.substring(1, str.lastIndex).split(",")
                .map { it.substringBefore("=") to it.substringAfter("=").toInt() }
                .let { categories ->

                    val categoryMap = listOf("x", "m", "a", "s")
                        .associateWith { category -> categories.first { (name, _) -> name == category }.second }


                    PartRange(
                        categoryMap["x"]!!,
                        categoryMap["m"]!!,
                        categoryMap["a"]!!,
                        categoryMap["s"]!!
                    )
                }

            fun of(map: Map<String, IntRange>) = PartRange(
                map["x"]!!,
                map["m"]!!,
                map["a"]!!,
                map["s"]!!
            )
        }
    }


    sealed class Rule(val match: (PartRange) -> Pair<PartRange, PartRange>) {
        companion object {
            fun of(str: String): Rule {
                val category = str.substring(0, 1)
                val operator = str.substring(1, 2)
                val value = str.substring(2).toInt()

                return when (operator) {
                    "<" -> Compare(category) { it < value }
                    ">" -> Compare(category) { it > value }
                    else -> error("unknown operator $operator")
                }
            }
        }
    }

    class Compare(private val category: String, private val predicate: (Int) -> Boolean) : Rule({ range ->
        range.split(category, predicate)
    })

    data object True : Rule({ it to PartRange.EMPTY })

    data class Workflow(val rules: List<Pair<Rule, String>>) {
        fun process(partRange: PartRange): Map<PartRange, String> {
            return rules.fold(emptyMap<PartRange, String>() to partRange) { (map, ranges), rule ->
                rule.first.match(ranges)
                    .let { (matched, denied) ->
                        val newMap = if (!matched.isEmpty()) {
                            map.add(matched, rule.second)
                        } else map
                        newMap to denied
                    }

            }.let { (map, denied) ->
                require(denied.isEmpty())
                map
            }
        }

        companion object {
            fun ofString(str: String): Workflow {
                return Workflow(str.split(",").map {
                    if (it.contains(':')) {
                        Rule.of(it.substringBefore(":")) to it.substringAfter(":")
                    } else {
                        True to it
                    }
                })
            }
        }
    }

    private fun Map<String, Workflow>.process(partRange: PartRange): List<PartRange> {
        val result = generateSequence(mapOf(partRange to "in")) { map ->
            map.map { (ratings, key) ->
                if (key == "A" || key == "R")
                    mapOf(ratings to key)
                else
                    this[key]?.process(ratings)
            }
                .filterNotNull()
                .flatMap { ratingsMap -> ratingsMap.entries.map { (key, value) -> key to value } }.toMap()
        }.first { it.values.all { value -> value in setOf("A", "R") } }

        return result.toList().filter { (_, status) -> status == "A" }.map { (range, _) -> range }

    }

    private fun List<String>.parse(): Pair<Map<String, Workflow>, List<PartRange>> {

        val workflows = this.takeWhile { it.isNotEmpty() }.associate { row ->
            val name = row.substringBefore("{")
            val rules = row.substring(row.indexOf("{") + 1, row.lastIndex)
            name to Workflow.ofString(rules)
        }

        val parts = this.takeLastWhile { it.isNotEmpty() }.map { PartRange.of(it) }

        return workflows to parts
    }


    override fun part1(input: List<String>): Number {
        val (workflows, parts) = input.parse()

        (1..10).partition { it % 2 == 0 }


        return parts.sumOf {
            workflows.process(it)
                .sumOf { ratings ->
                    ratings.sum()
                }
        }
    }

    override fun part2(input: List<String>): Number {
        val (workflows, _) = input.parse()


        val result = workflows.process(
            PartRange(
                1..4000,
                1..4000,
                1..4000,
                1..4000,
            )
        )

        return result.sumOf { ratings ->
            ratings.combinations()
        }

    }

    override fun main() {
        readInput("Day19_test").let { input ->
            checkWithTimer("Test Input P1", 19114) {
                part1(input)
            }
            checkWithTimer("Test Input P2", 167409079868000) {
                part2(input)
            }
        }

        readInput("Day19").let { input ->
            checkWithTimer("Input P1", 495298) {
                part1(input)
            }
            checkWithTimer("Input P2", 132186256794011) {
                part2(input)
            }
        }
    }
}
