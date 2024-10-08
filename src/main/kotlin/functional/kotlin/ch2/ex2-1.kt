package functional.kotlin.ch2

fun fib(i: Int): Long {
    tailrec fun go(i: Int, a: Long, b: Long): Long =
        if (i == 2) a + b
        else go(i - 1, b, a + b)

    return when (i) {
        0 -> 0
        1 -> 1
        else -> go(i, 0, 1)
    }
}