import kotlin.io.path.Path
import kotlin.io.path.readLines

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = Path("src/$name.txt").readLines()

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)

fun List<String>.println() {
    forEach(::println)
}

fun String.green(): String = green + this + reset
fun String.red() = red + this + reset
fun String.cyan() = cyan + this + reset

private const val red = "\u001B[31m"
private const val green = "\u001B[92m"
private const val cyan = "\u001b[36m"
private const val reset = "\u001B[0m"
    