package functional.kotlin.ch2

val <T> List<T>.tail: List<T>
    get() = drop(1)

val <T> List<T>.head: T
    get() = first()

fun <A> isSorted(aa: List<A>, order: (A, A) -> Boolean): Boolean {
    tailrec fun loop(first: A, rest: List<A>): Boolean =
        when {
            rest.isEmpty() -> true
            order(first, rest.head) == false -> false
            else -> loop(rest.head, rest.tail)
        }

    return if(aa.isEmpty()) true else loop(aa.head, aa.tail)
}