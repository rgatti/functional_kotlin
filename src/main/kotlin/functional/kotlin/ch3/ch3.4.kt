package functional.kotlin.ch3

/*
    foldRight -> f(xs[0], f(xs[1], f(xs[2], f(..., f(xs[n], z)))
    foldLeft -> f( f( f( f( ... f(z, xs[0]), x[1]), x[2]) ...)
*/

fun <A, B> foldRight(xs: List<A>, z: B, f: (A, B) -> B): B =
    when (xs) {
        is Nil -> z
        is Cons -> f(xs.head, foldRight(xs.tail, z, f))
    }

fun <A> empty(): List<A> = Nil

fun sum2(ints: List<Int>): Int =
    foldRight(ints, 0) { a, b -> a + b }

fun product2(dbs: List<Double>): Double =
    foldRight(dbs, 1.0) { a, b -> a * b }

/* 3.6 can product, using foldRight immediately halt and return 0.0 if it encounters 0.0?

No, because the applied function happens after expansion.

                  break
                 🡻
[1.0, 2.0, 3.0, 0.0, 4.0] =
    fn(1.0, fn(2.0, fn(3.0, fn(0.0, fn(4.0, 1.0)))))
*/

/* 3.8 compute length as foldRight */
fun <A> length(xs: List<A>): Int =
    foldRight(xs, 0) { _, l -> l + 1 }

/* 3.9 implement foldLeft as tail recursive */
tailrec fun <A, B> foldLeft(xs: List<A>, z: B, f: (B, A) -> B): B =
    when (xs) {
        is Nil -> z
        is Cons -> foldLeft(xs.tail, f(z, xs.head), f)
    }

/* 3.10 write sum, product, length as foldLeft */
fun sum3(ints: List<Int>) =
    foldLeft(ints, 0) { a, b -> a + b }

fun product3(dbs: List<Double>) =
    foldLeft(dbs, 1.0) { a, b -> if (b == 0.0) 0.0 else a * b }

fun <A> length1(xs: List<A>): Int =
    when (xs) {
        is Nil -> 0
        is Cons -> foldLeft(xs.tail, 1) { l, _ -> l + 1 }
    }

/* 3.11 write reverse as a fold */
fun <A> reverse(xs: List<A>): List<A> =
    when (xs) {
        is Nil -> empty()
        is Cons -> foldLeft(xs.tail, Cons(xs.head, Nil)) { a, b -> Cons(b, a) }
    }

/* 3.12 implement foldLeft in terms of foldRight and vis-verse */
fun <A, B> foldLeft1(xs: List<A>, z: B, f: (B, A) -> B): B =
    foldRight(
        // reverse using only foldRight
        foldRight(xs, empty<A>()) { a, b ->
            foldRight(b, List.of(a)) { aa, bb -> Cons(aa, bb) }
        }, z
    ) { a, b -> f(b, a) }

fun <A, B> foldRight1(xs: List<A>, z: B, f: (A, B) -> B): B =
    // well ... reverse is implemented as a foldLeft ;)
    foldLeft(reverse(xs), z) { a, b -> f(b, a) }

/* 3.13 implement append as a fold */
fun <A> append(xs: List<A>, a: A): List<A> =
    when (xs) {
        is Nil -> List.of(a)
        is Cons -> foldRight(xs, List.of(a)) { aa, b -> Cons(aa, b) }
    }

/* 3.14 concatenate a list of lists into a single list with linear performance */
fun <A> flatten(xs: List<List<A>>): List<A> =
    foldRight(xs, empty()) { a, b -> foldRight(a, b) { aa, bb -> Cons(aa, bb) } }

/* 3.15 write a pure function that transforms a list of ints by adding 1 */
fun add1(xs: List<Int>): List<Int> = foldRight(xs, empty()) { e, b -> Cons(e + 1, b) }

/* 3.16 convert each element of a list to a string */
fun doubleToString(xs: List<Double>): List<String> = foldRight(xs, empty()) { e, b -> Cons(e.toString(), b) }

/* 3.17 write a map func that generalizes mutating each element of a list */
fun <A, B> map(xs: List<A>, f: (A) -> B): List<B> = foldRight(xs, empty()) { e, b -> Cons(f(e), b) }

/* 3.18 write a filter function to remove elements unless they satisfy a predicate */
// filter(List.of(1, 2, 3, 4, 5, 6)) { i -> i % 2 == 0 }
fun <A> filter(xs: List<A>, f: (A) -> Boolean): List<A> =
    foldRight(xs, empty()) { e, b -> if (f(e)) Cons(e, b) else b }

/* 3.19 write flatMap */
fun <A, B> flatMap(xs: List<A>, f: (A) -> List<B>): List<B> =
    foldRight(xs, empty()) { origE, buf ->
        foldRight(f(origE), buf) { mapE, buf2 -> Cons(mapE, buf2) }
    }

/* 3.20 use flatMap to implement filter */
// Why do this, I'm missing something?
fun <A> filter2(xs: List<A>, f: (A) -> Boolean): List<A> =
    flatMap(xs) { e -> if (f(e)) Cons(e, Nil) else empty() }

/* 3.21 write a function to add two lists together by adding corresponding elements */
fun addLists(xs1: List<Int>, xs2: List<Int>): List<Int> {
    fun go(xss1: List<Int>, xss2: List<Int>, buf: List<Int>): List<Int> =
        if (xss1 is Nil) foldRight(xss2, buf) { e, b -> Cons(e, b) }
        else if (xss2 is Nil) foldRight(xss1, buf) { e, b -> Cons(e, b) }
        else {
            xss1 as Cons // cast for the compiler
            xss2 as Cons
            Cons(xss1.head + xss2.head, go(xss1.tail, xss2.tail, buf))
        }

    return go(xs1, xs2, empty())
}

/* 3.22 write zipWith */
// This isn't quite the same as the original b/c the final List is the shortest of xs1 / xs2.
fun <A, B, C> zipWith(xs1: List<A>, xs2: List<B>, f: (A, B) -> C): List<C> {
    fun go(xss1: List<A>, xss2: List<B>, buf: List<C>): List<C> =
        when {
            xss1 is Cons && xss2 is Cons -> Cons(f(xss1.head, xss2.head), go(xss1.tail, xss2.tail, buf))
            else -> buf
        }

    return go(xs1, xs2, empty())
}

/* 3.23 implement hasSubsequence to check if a List contains another List as a subsequence */
tailrec fun <A> hasSubsequence(xs: List<A>, sub: List<A>): Boolean =
    when (xs) {
        is Nil -> sub is Nil
        is Cons -> when (sub) {
            is Nil -> true
            is Cons -> {
                val (nextXs, nextSub) = if (xs.head == sub.head) xs.tail to sub.tail else xs.tail to sub
                hasSubsequence(nextXs, nextSub)
            }
        }
    }

fun main() {
}