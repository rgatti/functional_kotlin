package functional.kotlin.ch5

import functional.kotlin.ch3.List
import functional.kotlin.ch3.Nil
import kotlin.time.measureTimedValue
import functional.kotlin.ch3.Cons as LCons

// 5.1 convert a Stream to a List
fun <A> Stream<A>.toList(): List<A> =
    foldRight({ Nil as List<A> }) { h, l -> LCons(h, l()) }

// 5.2 write take(n) to return the n elements of a Stream and drop(n) to skip the first n elements
fun <A> Stream<A>.take(n: Int): Stream<A> =
    when (this) {
        is Empty -> Empty
        is Cons -> if (n == 0) Empty else Cons(head) { tail().take(n - 1) }
    }

tailrec fun <A> Stream<A>.drop(n: Int): Stream<A> =
    when (this) {
        is Empty -> Empty
        is Cons -> if (n == 0) this else tail().drop(n - 1)
    }

fun <A> Stream<A>.takeWhile(p: (A) -> Boolean): Stream<A> =
    when (this) {
        is Empty -> this
        is Cons -> if (p(head())) cons(head) { tail().takeWhile(p) } else Empty
    }

fun main() {
    measureTimedValue {
        Stream.of(*List(1_000) { it + 1 }.toTypedArray()).takeWhile() { it < 50 }.toList().print()
    }.duration
        .inWholeMilliseconds
        .let(::println)
}
