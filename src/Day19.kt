fun main() {
    Day19.main()
}

typealias CategoryRatings = Map<String, IntRange>

object Day19 : Solution() {
    sealed class Rule(val match: (CategoryRatings) -> Pair<CategoryRatings, CategoryRatings>) {


        companion object {
            fun of(str: String): Rule {
                val category = str.substring(0, 1)
                val operator = str.substring(1, 2)
                val value = str.substring(2).toInt()

                return when (operator) {
                    "<" -> Smaller(category, value)
                    ">" -> Greater(category, value)
                    else -> error("unknown operator $operator")
                }
            }
        }
    }

    class Greater(private val category: String, private val value: Int) :
        Rule({ ratings ->

            val (yes, no) = ratings[category]!!.split { it > value }

            buildMap {
                ratings.filter { (cat, _) -> cat != category }
                    .forEach { (cat, range) -> set(cat, range) }
                set(category, yes)

            } to buildMap {
                ratings.filter { (cat, _) -> cat != category }
                    .forEach { (cat, range) -> set(cat, range) }
                set(category, no)
            }
        }) {
        override fun toString(): String {
            return "$category > $value"
        }
    }

    class Smaller(private val category: String, private val value: Int) :
        Rule({ ratings ->

            val (yes, no) = ratings[category]!!.split { it < value }

            buildMap {
                ratings.filter { (cat, _) -> cat != category }
                    .forEach { (cat, range) -> set(cat, range) }
                set(category, yes)

            } to buildMap {
                ratings.filter { (cat, _) -> cat != category }
                    .forEach { (cat, range) -> set(cat, range) }
                set(category, no)
            }
        }) {
        override fun toString(): String {
            return "$category < $value"
        }
    }

    data object True : Rule({ it to emptyMap() })

    data class Workflow(val rules: List<Pair<Rule, String>>) {
        fun process(ratings: CategoryRatings): Map<CategoryRatings, String> {
            return rules.fold(emptyMap<CategoryRatings, String>() to ratings) { (map, ranges), rule ->
                rule.first.match(ranges)
                    .let { (matched, denied) ->
                        val newMap = if (matched.values.none { it.isEmpty() }) {
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

    private fun Map<String, Workflow>.process(startRatings: CategoryRatings): List<CategoryRatings> {
        val result = generateSequence(mapOf(startRatings to "in")) { map ->
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

    private fun CategoryRatings.combinations(): Long =
        values.map { range -> range.count().toLong() }.reduce { a, b -> a * b }

    private fun CategoryRatings.sum(): Int =
        values.sumOf { it.first }

    override fun part2(input: List<String>): Number {
        val (workflows, _) = input.parse()


        val result = workflows.process(
            mapOf(
                "x" to 1..4000,
                "m" to 1..4000,
                "a" to 1..4000,
                "s" to 1..4000,
            )
        )

        return result.sumOf { ratings ->
            ratings.combinations()
        }

    }

    private fun List<String>.parse(): Pair<Map<String, Workflow>, List<Map<String, IntRange>>> {
        fun parsePart(str: String) = str.substring(1, str.lastIndex).split(",")
            .map { it.substringBefore("=") to it.substringAfter("=").toInt() }
            .let { list ->
                val categories = listOf("x", "m", "a", "s")
                buildMap {
                    categories.forEach { category ->
                        val value = list.first { (name, _) -> name == category }.second
                        put(category, value..value)
                    }
                }
            }


        val workflows = this.takeWhile { it.isNotEmpty() }.associate { row ->
            val name = row.substringBefore("{")
            val rules = row.substring(row.indexOf("{") + 1, row.lastIndex)
            name to Workflow.ofString(rules)
        }

        val parts = this.takeLastWhile { it.isNotEmpty() }.map { parsePart(it) }

        return workflows to parts
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
