package functional.kotlin.ch3

sealed interface List<out A> {
    companion object {
        fun <A> of(vararg l: A): List<A> {
            val tail = l.sliceArray(1 until l.size)
            return if (l.isEmpty()) Nil else Cons(l[0], of(*tail))
        }
    }

    fun print() {
        println(toString())
    }
}

data object Nil : List<Nothing>
{
    override fun toString(): String = "[∅]"
}

class Cons<out A>(val head: A, val tail: List<A>) : List<A>
{
    override fun toString(): String {
        tailrec fun go(l: List<A>, acc: StringBuilder = StringBuilder()): String =
            when (l) {
                is Nil -> acc.append("∅").toString()
                is Cons -> go(l.tail, acc.append(l.head).append(" -> "))
            }

        return "[${go(this)}]"
    }
}
