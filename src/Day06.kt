import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.sqrt

fun main() {
    Day06().main()
}


class Day06 : Solution("Day06", "Day06_test", (288 to 71503)) {
    override fun part1(input: List<String>): Number {
        return input.parse1()
            .map { (time, distance) ->
                solve(time, distance)
            }.reduce(Long::times)
    }

    override fun part2(input: List<String>): Number {
        return input.parse2()
            .let { (time, distance) -> solve(time, distance) }
    }

    private fun solve(time: Long, recordDistance: Long): Long {
        val disc = (time * time - 4 * recordDistance).toDouble()

        val root1 = ((-time + sqrt(disc)) / -2).adjustToLong(Direction.UP)
        val root2 = ((-time - sqrt(disc)) / -2).adjustToLong(Direction.DOWN)

        return root2 - root1 + 1
    }

    private fun Double.adjustToLong(direction: Direction): Long {
        // when value is a whole number, we need to subtract/add one
        return if (floor(this) == ceil(this)) {
            (this + (direction.value)).toLong()
        } else if (direction == Direction.DOWN) {
            floor(this).toLong()
        } else {
            ceil(this).toLong()
        }
    }

    private enum class Direction(val value: Int) {
        UP(1),
        DOWN(-1)
    }
}

fun List<String>.parse1(): List<Pair<Long, Long>> {
    return map { line ->
        line.substringAfter(":")
            .split(" ")
            .filter { it.isNotBlank() }
            .map { it.toLong() }
    }.let { (timeList, distList) -> timeList zip distList }
}
fun List<String>.parse2(): Pair<Long, Long> {
    return map { line ->
        line.substringAfter(":").replace(" ", "").toLong()
    }.let { (time, dist) -> time to dist }
}

