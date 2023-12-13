import kotlin.io.path.Path
import kotlin.io.path.readLines
import kotlin.time.measureTimedValue

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


fun withTimer(msg: String, block: () -> Number) {
    val (value, time) = measureTimedValue { block() }

    val timeStr = "${time.inWholeMilliseconds}ms".cyan()
    println("$msg\t$timeStr\t$value")
}

fun checkWithTimer(msg: String, expected: Number, block: () -> Number) {
    val (value, time) = measureTimedValue { block() }

    val passed = value.toDouble() == expected.toDouble()

    val timeInMs = "${time.inWholeMilliseconds}ms".cyan()

    val valueStr =
            if (passed) "✅ $value".green()
            else "❌ $value should be $expected".red()

    println("$msg\t$timeInMs\t$valueStr")
}