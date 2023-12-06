fun main() = Day05().main()

data class RangeMap(val source: List<LongRange>, val destination: List<LongRange>) {
    operator fun get(from: Long): Long {
        for ((i, src) in source.withIndex()) {
            val offset = if (from >= src.first && from <= src.last) {
                from - src.first
            } else null

            if (offset != null) {
                return destination[i].first + offset
            }
        }
        return from
    }

    companion object {
        fun fromLines(lines: List<String>): RangeMap {
            val sources = mutableListOf<LongRange>()
            val destinations = mutableListOf<LongRange>()

            lines.forEach { line ->
                val (dst, src, len) = line.split(" ").map { it.toLong() }
                sources.add(src until src + len)
                destinations.add(dst until dst + len)
            }

            return RangeMap(sources.toList(), destinations.toList())
        }
    }
}

class Day05 : Solution("Day05", "Day05_test", (35 to 46)) {
    override fun part1(input: List<String>): Number {
        val blocks = input.toBlocks()

        val seeds = blocks.first().first()
                .split("seeds: ", " ")
                .filter { it.isNotBlank() }
                .map { it.toLong() }


        val maps = blocks.drop(1)
                .map { block -> RangeMap.fromLines(block.drop(1)) }

        return seeds.minOf { seed ->
            maps.pipe(seed) { value -> get(value) }
        }
    }

    override fun part2(input: List<String>): Number {
        val blocks = input.toBlocks()

        val seedRanges = blocks.first().first()
                .split("seeds: ", " ")
                .filter { it.isNotBlank() }
                .map { it.toLong() }
                .chunked(2)
                .map { (start, len) -> start until start + len }

        val maps = blocks.drop(1)
                .map { block -> RangeMap.fromLines(block.drop(1)) }

        return seedRanges.minOf { range ->
            range.minOf { maps.pipe(it) { value -> get(value) } }
        }
    }

    private fun List<String>.toBlocks(): List<List<String>> {
        val blocks = mutableListOf<List<String>>()

        var copy = toList()
        var index = copy.indexOf("")

        do {
            val block = copy.slice(0 until index)
            blocks.add(block)
            copy = copy.drop(index + 1)
            index = copy.indexOf("")
        } while (index != -1)
        blocks.add(copy)

        return blocks.toList()
    }

    private fun <T, R> List<T>.pipe(start: R, block: T.(R) -> R): R {
        var value = start
        forEach {
            value = it.block(value)
        }
        return value
    }
}
