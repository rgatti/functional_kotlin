package functional.kotlin.ch5

import functional.kotlin.ch3.Cons
import functional.kotlin.ch4.*
import kotlin.time.measureTimedValue

// 5.4 implement forAll() which checks that all elements in the Stream match a predicate
fun <A> Stream<A>.forAll(p: (A) -> Boolean): Boolean =
    foldRight({ true }) { a, b -> p(a) && b() }

// 5.5 use foldRight to implement takeWhile
fun <A> Stream<A>.takeWhile1(p: (A) -> Boolean): Stream<A> =
    foldRight({ empty() }) { h, s -> if (p(h)) cons({ h }, s) else Empty }

// 5.6 implement headOption using foldRight
fun <A> Stream<A>.headOption1(): Option<A> =
    foldRight({ None as Option<A> }) { h, _ -> Some(h) }

// 5.7 implement map, filter, append, and flatMap using foldRight
fun <A> Stream<A>.filter(f: (A) -> Boolean): Stream<A> =
    foldRight({ empty() }) { h, s -> if (f(h)) cons({ h }, s) else s() }

fun <A, B> Stream<A>.map(f: (A) -> B): Stream<B> =
    foldRight(
        { empty() },
        { h: A, s: () -> Stream<B> ->
            cons({ f(h) }, s)
        })

fun <A> Stream<A>.append(sa: () -> Stream<A>): Stream<A> =
    foldRight(sa) { h, t -> cons({ h }, t) }

//fun <A, B> Stream<A>.flatMap(f: (A) -> Stream<B>): Stream<B> =
//    foldRight( { empty() }) { origE, buf ->
//        foldRight(f(origE), buf) { mapE, buf2 -> cons({ mapE }, buf2) }
//    }

fun main() {
    Stream.of(1, 2, 3, 4)
        .map { it + 10 }
        .filter { it % 2 == 0 }
        .map { it * 3 }
        .toList()
        .print()

    /*

    map f={it + 10} (1 : (2 : (3 : (4 : []))))
    filter f={it % 2 == 0} (11 :



     */
}