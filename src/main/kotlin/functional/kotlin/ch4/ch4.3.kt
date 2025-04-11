package functional.kotlin.ch4

import functional.kotlin.ch3.*
import kotlin.math.pow
import functional.kotlin.ch3.List as List

sealed interface Option<out A>
data class Some<A>(val get: A) : Option<A>
data object None : Option<Nothing>

fun <A, B> Option<A>.map(f: (A) -> B): Option<B> =
    when (this) {
        is None -> None
        is Some -> Some(f(get))
    }

fun <A> Option<A>.getOrElse(default: () -> A): A =
    when (this) {
        is None -> default()
        is Some -> get
    }

fun <A, B> Option<A>.flatMap(f: (A) -> Option<B>): Option<B> = map { f(it) }.getOrElse { None }

fun <A> Option<A>.filter(f: (A) -> Boolean): Option<A> = flatMap { if (f(it)) Some(it) else None }

// the value has to be double wrapped for getOrElse() to decompose it into a final result
fun <A> Option<A>.orElse(ob: () -> Option<A>): Option<A> = map { Some(it) }.getOrElse { ob() }

fun sum(xs: List<Double>) = foldLeft(xs, 0.0) { a, b -> a + b }

fun mean(xs: List<Double>): Option<Double> =
    if (length1(xs) == 0) None
    else Some(sum(xs) / length1(xs))

// https://www.calculatorsoup.com/calculators/statistics/variance-calculator.php
fun sum4(xs: List<Double>) = foldLeft(xs, 0.0) { a, b -> a + b }

fun variance(xs: List<Double>): Option<Double> =
    Some(xs)
        .filter { length1(it) > 1 }
        .flatMap { mean(it) }
        .flatMap { m ->
            Some(sum4(map(xs) { (it - m).pow(2) }) / (length1(xs) - 1))
        }

/* 4.3 write a map2() that combines two Option */
fun <A, B, C> map2(a: Option<A>, b: Option<B>, f: (A, B) -> C): Option<C> =
    a.flatMap { a1 ->
        b.map { b1 ->
            f(a1, b1)
        }
    }

/* 4.4 write sequence() that combines a List<Option> into an Option<List> */
fun <A> sequence0(xs: List<Option<A>>): Option<List<A>> {
//    return Some(xs.map {
//        when (it) {
//            is None -> return None
//            is Some -> it.get
//        }
//    })
    TODO()
}

fun <A> sequence1(xs: List<Option<A>>): Option<List<A>> =
    when (xs) {
        is Nil -> Some(empty())
        is Cons -> sequence(xs.tail).flatMap { l -> xs.head.map { Cons(it, l) } }
    }

fun <A> sequence(xs: List<Option<A>>): Option<List<A>> =
    foldRight(xs, Some(empty())) { a: Option<A>, l: Option<List<A>> ->
        //println("a: $a, l: $l")
        l.flatMap { ll -> a.map { Cons(it, ll) } } //map2(a, l) { aa, ll -> Cons(aa, ll) }
    }

fun <A, B> traverse(xs: List<A>, f: (A) -> Option<B>): Option<List<B>> =
    foldRight(xs, Some(empty())) { a: A, l: Option<List<B>> ->
        map2(f(a), l) { fa, ll -> Cons(fa, ll) }
    }

fun main() {
    val e = empty<Option<Int>>()
    val xs = List.of(Some(0), Some(1), None, Some(2))

    sequence(xs)
        .map { it.print() }

    val l = List.of(1, 2, 3)
    val f: (Int) -> Option<Int> = {
        Some(it * 2)
       //if(it == 2) None else Some(it)
    }
    traverse(l, f).map { println(it) }
}
