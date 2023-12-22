fun main() {
    Day15.main()
}

object Day15 : Solution() {

    data class Lens(val label: String, val focalLength: Int)
    class Box() {
        val lenses = mutableListOf<Lens>()

        fun remove(label: String) {
            lenses.firstOrNull { it.label == label }?.let { lenses.remove(it) }
        }

        fun put(lens: Lens) {
            lenses.firstOrNull { it.label == lens.label }
                    ?.let { oldLens -> lenses[lenses.indexOf(oldLens)] = lens }
                    ?: lenses.add(lens)
        }
    }

    sealed class Operation(val label: String)
    class Remove(label: String) : Operation(label)
    class Put(label: String, val focal: Int) : Operation(label)

    private fun List<String>.parse() = joinToString("").split(",")

    private fun String.asciHash(): Int {
        return fold(0) { acc, c -> ((acc + c.code) * 17) % 256 }
    }

    override fun part1(input: List<String>): Number {
        return input.parse().sumOf { it.asciHash() }
    }

    override fun part2(input: List<String>): Number {
        val boxes = MutableList(256) { Box() }
        val sequence = input.parse().map { str ->
            if (str.contains("=")) {
                val label = str.substringBefore("=")
                val focal = str.substringAfter("=").toInt()
                Put(label, focal)
            } else {
                Remove(str.substringBefore("-"))
            }
        }

        sequence.forEach { operation ->
            val hash = operation.label.asciHash()

            when (operation) {
                is Put -> boxes[hash].put(Lens(operation.label, operation.focal))
                is Remove -> boxes[hash].remove(operation.label)
            }
        }

        return boxes.mapIndexed { i, box ->
            box.lenses.mapIndexed { j, lens -> (j + 1) * lens.focalLength }.sumOf { it * (i + 1) }
        }.sum()

    }

    override fun main() {

        checkWithTimer("HASH test", 52) {
            "HASH".asciHash()
        }

        readInput("Day15_test").let { input ->
            checkWithTimer("Test Input P1", 1320) {
                part1(input)
            }

            checkWithTimer("Test Input P2", 145) {
                part2(input)
            }
        }
        println("")
        readInput("Day15").let { input ->
            checkWithTimer("Main Input p1", 511416) {
                part1(input)

            }
            checkWithTimer("Main Input P2", 290779) {
                part2(input)
            }

        }
    }
}

