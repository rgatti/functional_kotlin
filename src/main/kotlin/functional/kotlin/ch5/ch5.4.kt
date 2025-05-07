package functional.kotlin.ch5

import functional.kotlin.ch4.Option
import functional.kotlin.ch4.Some
import functional.kotlin.ch4.getOrElse
import functional.kotlin.ch4.map

fun ones(): Stream<Int> = cons({ 1 }, { ones() })

fun ones1(): Stream<Int> = unfold(1, { some(Pair(it, it)) })

fun <A> constant(a: A): Stream<A> = cons({ a }, { constant(a) })

fun <A> constant1(a: A): Stream<A> = unfold(a, { some(Pair(a, a)) })

fun from(n: Int): Stream<Int> = cons({ n }, { from(n + 1) })

fun fib(): Stream<Int> {
    fun go(f: Int, s: Int): Stream<Int> =
        cons({ f }, { go(s, f + s) })
    return go(0, 1)
}

fun fib1(): Stream<Int> = unfold(
    Pair(0, 1),
    { s -> some(Pair(s.first, Pair(s.second, s.first + s.second))) })

fun <A, S> unfold(z: S, f: (S) -> Option<Pair<A, S>>): Stream<A> =
    f(z).map { (v, s) -> cons({ v }, { unfold(s, f) }) }.getOrElse { empty() }

fun <A> some(get: A): Option<A> = Some(get)

fun main() {
    constant1(6).take(7)
        .toList()
        .print()
}