package functional.kotlin.ch3

sealed interface Tree<out A>
data class Leaf<A>(val value: A) : Tree<A>
data class Branch<A>(val left: Tree<A>, val right: Tree<A>) : Tree<A>

/* 3.24 write a function size() that counts the number of nodes (leaves and branches) in a tree. */
fun size(t: Tree<*>): Int =
    when (t) {
        is Leaf -> 1
        is Branch -> 1 + size(t.left) + size(t.right)
    }

/* 3.25 write a function maximum() that returns the maximum element is a Tree<Int>. */
fun maximum(t: Tree<Int>): Int =
    when (t) {
        is Leaf -> t.value
        is Branch -> maxOf(maximum(t.left), maximum(t.right))
    }

/* 3.26 write a function depth() that returns the maximum path length from the root of a tree to any leaf. */
fun depth(t: Tree<*>): Int =
    when (t) {
        is Leaf -> 0
        is Branch -> maxOf(depth(t.left) + 1, depth(t.right) + 1)
    }

/* 3.27 write a function map(), analogous to the method of the same name on List, that modifies each element in a tree
with a given function. */
fun <A, B> map(t: Tree<A>, fn: (A) -> B): Tree<B> =
    when (t) {
        is Leaf -> Leaf(fn(t.value))
        is Branch -> Branch(map(t.left, fn), map(t.right, fn))
    }

/* 3.28 write a fold() that abstracts the previous fun(s) over Tree. */
fun <A, B> fold(t: Tree<A>, l: (A) -> B, b: (B, B) -> B): B =
    when (t) {
        is Leaf -> l(t.value)
        is Branch -> b(fold(t.left, l, b), fold(t.right, l, b))
    }

fun <A> sizeF(t: Tree<A>): Int = fold(t, { 1 }, { a, b -> 1 + a + b })
fun maximumF(t: Tree<Int>): Int = fold(t, { it }, { a, b -> maxOf(a, b) })
fun <A> depthF(t: Tree<A>): Int = fold(t, { 0 }, { a, b -> maxOf(a + 1, b + 1) })
fun <A, B> mapF(t: Tree<A>, f: (A) -> B): Tree<B> =
    fold(t, { Leaf(f(it)) }, { a: Tree<B>, b: Tree<B> -> Branch(a, b) })

//

/*
      /\
     /\ 3
    1 /\
     2 /\
      3  8
*/
val t = Branch(
    Branch(
        Leaf(1),
        Branch(
            Leaf(2),
            Branch(
                Leaf(3),
                Leaf(8),
            )
        )
    ),
    Leaf(3)
)

fun main() {
}