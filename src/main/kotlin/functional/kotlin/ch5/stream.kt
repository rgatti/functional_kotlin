package functional.kotlin.ch5

import functional.kotlin.ch4.None
import functional.kotlin.ch4.Option
import functional.kotlin.ch4.Some

sealed interface Stream<out A> {
    companion object {
        fun <A> of(vararg xs: A): Stream<A> =
            if (xs.isEmpty()) empty()
            else cons({ xs[0] }, { of(*xs.sliceArray(1 until xs.size)) })
    }
}

data class Cons<out A>(
    val head: () -> A,
    val tail: () -> Stream<A>
) : Stream<A>

fun <A> cons(hd: () -> A, tl: () -> Stream<A>): Stream<A> {
    val head: A by lazy(hd)
    val tail: Stream<A> by lazy(tl)
    return Cons({ head }, { tail })
}

fun <A> empty(): Stream<A> = Empty

data object Empty : Stream<Nothing>

fun <A> Stream<A>.headOption(): Option<A> =
    when (this) {
        is Empty -> None
        is Cons -> Some(head())
    }

tailrec fun <A, B> foldLeft(xs: Stream<A>, z: B, f: (B, A) -> B): B =
    when (xs) {
        is Empty -> z
        is Cons -> foldLeft(xs.tail(), f(z, xs.head()), f)
    }

fun <A, B> Stream<A>.foldRight(z: () -> B, f: (A, () -> B) -> B): B =
    when (this) {
        is Empty -> z()
        is Cons -> f(head(), { tail().foldRight(z, f) })
    }

fun <A> reverse(xs: Stream<A>): Stream<A> =
    when (xs) {
        is Empty -> Empty
        is Cons -> foldLeft(xs.tail(), Cons(xs.head) { Empty }) { a, b -> Cons({ b }, { a }) }
    }
