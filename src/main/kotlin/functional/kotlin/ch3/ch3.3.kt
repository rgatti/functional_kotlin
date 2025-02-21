package functional.kotlin.ch3

/** 3.1 remove the first element from the list */
fun <A> tail(xs: List<A>): List<A> = if (xs is Cons) xs.tail else Nil

/** 3.2 replace first element of list with different value */
fun <A> setHead(xs: List<A>, x: A): List<A> = if (xs is Cons) Cons(x, xs) else List.of(x)

/** 3.3 remove first n elements from list */
fun <A> drop(l: List<A>, n: Int): List<A> =
    if (n == 0) {
        l
    } else {
        when (l) {
            is Nil -> Nil
            is Cons -> drop(l.tail, n - 1)
        }
    }

/** 3.4 drop while predicate matches */
fun <A> dropWhile(l: List<A>, f: (A) -> Boolean): List<A> =
    when (l) {
        is Nil -> l
        is Cons -> if (f(l.head)) dropWhile(l.tail, f) else l
    }

/** 3.5 returns a list of all but the last element */
fun <A> init(l: List<A>): List<A> =
    when (l) {
        is Nil -> l
        is Cons -> when (l.tail) {
            is Nil -> Nil
            is Cons -> Cons(l.head, init(l.tail))
        }
    }

fun main() {
    val l = List.of(1, 2, 3, 4, 5)

    tail(l).print() // 2 -> 3 -> 4 -> 5 -> ∅
    setHead(l, 0).print() // 0 -> 1 -> 2 -> 3 -> 4 -> 5 -> ∅
    drop(l, 2).print() // 3 -> 4 -> 5 -> ∅
    dropWhile(l) { x -> x < 4 }.print() // 4 -> 5 -> ∅
    init(l).print() // 1 -> 2 -> 3 -> 4 -> ∅
}