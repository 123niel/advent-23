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
operator fun <T> List<List<T>>.get(x: Int, y: Int) = this[y][x]
operator fun <T> List<List<T>>.get(vec: Vec) = this[vec.x.toInt(), vec.y.toInt()]

operator fun Array<IntArray>.get(vec: Vec) = this[vec.y.toInt()][vec.x.toInt()]

fun List<String>.transpose(): List<String> {
    return first().indices.map { i -> this.map { line -> line[i] } }
            .map { it.joinToString("") }
}

enum class Direction { N, E, S, W }

fun Direction.flip() = when (this) {
    Direction.E -> Direction.W
    Direction.N -> Direction.S
    Direction.W -> Direction.E
    Direction.S -> Direction.N
}

fun Direction.left() = when(this) {
    Direction.E -> Direction.N
    Direction.N -> Direction.W
    Direction.W -> Direction.S
    Direction.S -> Direction.E
}

fun Direction.right() = when(this) {
    Direction.E -> Direction.S
    Direction.N -> Direction.E
    Direction.W -> Direction.N
    Direction.S -> Direction.W
}

fun Vec.north(n: Int = 1) = Vec(x, y - n)
fun Vec.east(n: Int = 1) = Vec(x + n, y)
fun Vec.south(n: Int = 1) = Vec(x, y + n)
fun Vec.west(n: Int = 1) = Vec(x - n, y)

fun Vec.neighbour(dir: Direction, n: Int = 1): Vec {
    return when (dir) {
        Direction.E -> east(n)
        Direction.N -> north(n)
        Direction.W -> west(n)
        Direction.S -> south(n)
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

inline infix fun <reified T> Vec.isIn(grid: List<T>) = when (T::class) {
    String::class -> x in (grid.first() as String).indices && y in grid.indices
    List::class -> x in (grid.first() as List<*>).indices && y in grid.indices
    else -> error("not a grid")
}

infix fun Vec.isIn(grid: Array<IntArray>) = x in grid.first().indices && y in grid.indices


fun Pair<Vec, Vec>.distance(): Long =
        abs(first.x - second.x) + abs(first.y - second.y)

fun Vec.distance(other: Vec): Long =
        abs(this.x - other.x) + abs(this.y - other.y)

fun <K, V> Map<K, V>.add(key: K, value: V) = buildMap {
    putAll(this@add)
    put(key, value)
}

fun IntRange.split(intPredicate: (Int) -> Boolean): Pair<IntRange, IntRange> =
    this.partition(intPredicate).let { (yes, no) ->
        yes.toRange() to no.toRange()
    }

fun List<Int>.toRange(): IntRange = if (isEmpty()) IntRange.EMPTY else min()..max()

