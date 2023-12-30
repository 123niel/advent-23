import kotlin.math.abs

fun IntRange.boundTo(min: Int? = null, max: Int? = null): IntRange {
    val newStart = min?.let { start.coerceAtLeast(it) } ?: start
    val newEnd = max?.let { endInclusive.coerceAtMost(it) } ?: endInclusive
    return newStart..newEnd
}

data class Vec(val x: Long, val y: Long) {
    constructor(x: Int, y: Int) : this(x.toLong(), y.toLong())
}

operator fun List<String>.get(x: Int, y: Int) = get(y)[x]
operator fun List<String>.get(vec: Vec) = get(vec.x.toInt(), vec.y.toInt())


fun List<String>.transpose(): List<String> {
    return first().indices.map { i -> this.map { line -> line[i] } }
            .map { it.joinToString("") }
}

enum class Direction { N, E, S, W }

fun Vec.north() = Vec(x, y - 1)
fun Vec.east() = Vec(x + 1, y)
fun Vec.south() = Vec(x, y + 1)
fun Vec.west() = Vec(x - 1, y)

fun Vec.neighbour(dir: Direction): Vec {
    return when (dir) {
        Direction.E -> east()
        Direction.N -> north()
        Direction.W -> west()
        Direction.S -> south()
    }
}

fun Vec.neighbours(): List<Vec> = listOf(
        east(),
        north(),
        west(),
        south(),
)

fun Vec.neighboursMap(): Map<Direction, Vec> = mapOf(
    Direction.E to east(),
    Direction.N to north(),
    Direction.W to west(),
    Direction.S to south()
)

infix fun Vec.isIn(grid: List<String>) = x in grid.first().indices && y in grid.indices

fun Pair<Vec, Vec>.distance(): Long =
        abs(first.x - second.x) + abs(first.y - second.y)

fun Vec.distance(other: Vec): Long =
        abs(this.x - other.x) + abs(this.y - other.y)
