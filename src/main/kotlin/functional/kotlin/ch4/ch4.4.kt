package functional.kotlin.ch4

import functional.kotlin.ch3.Cons
import functional.kotlin.ch3.List
import functional.kotlin.ch3.empty
import functional.kotlin.ch3.foldRight

sealed interface Either<out E, out A>
data class Left<out E>(val value: E) : Either<E, Nothing>
data class Right<out A>(val value: A) : Either<Nothing, A>

/* 4.6 implement map(), flatMap(), orElse(), map2() on Either */
fun <E, A, B> Either<E, A>.map(f: (A) -> B): Either<E, B> =
    when (this) {
        is Left -> this
        is Right -> Right(f(value))
    }

fun <E, A> Either<E, A>.getOrElse(default: () -> A): A =
    when (this) {
        is Left -> default()
        is Right -> value
    }

fun <E, A> Either<E, A>.orElse(ob: () -> Either<E, A>): Either<E, A> =
    map { Right(it) }.getOrElse { this }

fun <E, A, B> Either<E, A>.flatMap(f: (A) -> Either<E, B>): Either<E, B> =
    when (this) {
        is Left -> this
        is Right -> f(value)
    }

fun <E, A, B, C> map2(a: Either<E, A>, b: Either<E, B>, f: (A, B) -> C): Either<E, C> =
    a.flatMap { a1 -> b.map { b1 -> f(a1, b1) } }

/* 4.7 implement sequence() and traverse() for Either */
fun <E, A> sequence(xs: List<Either<E, A>>): Either<E, List<A>> =
    foldRight(xs, Right(empty())) { a: Either<E, A>, l: Either<E, List<A>> ->
        l.flatMap { ll: List<A> -> a.map { Cons(it, ll) } }
    }

fun <E, A, B> traverse(xs: List<A>, f: (A) -> Either<E, B>): Either<E, List<B>> =
    foldRight(xs, Right(empty())) { a: A, l: Either<E, List<B>> ->
        map2(f(a), l) { fb, ll -> Cons(fb, ll) }
    }

/* 4.8 can map2() be changed to return both errors */
fun <E, A, B, C> map2_(a: Either<E, A>, b: Either<E, B>, f: (A, B) -> C): Either<List<E>, C> {
    TODO()
}
