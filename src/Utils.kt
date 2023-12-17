fun IntRange.boundTo(min: Int? = null, max: Int? = null): IntRange {
    val newStart = min?.let { start.coerceAtLeast(it) } ?: start
    val newEnd = max?.let { endInclusive.coerceAtMost(it) } ?: endInclusive
    return newStart..newEnd
}

data class Vec<T : Number>(val x: T, val y: T)

operator fun List<String>.get(x: Int, y: Int) = get(y)[x]
operator fun <T : Number> List<String>.get(vec: Vec<T>) = get(vec.y.toInt(), vec.x.toInt())


fun List<String>.transpose(): List<String> {
    return first().indices.map { i -> this.map { line -> line[i] } }
            .map { it.joinToString("") }
}