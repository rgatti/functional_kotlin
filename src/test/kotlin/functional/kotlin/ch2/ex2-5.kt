package functional.kotlin.ch2

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class `ex2-5` {
    @Test
    fun `compose test`() {
        val f = {b:String -> "$b, nice to meet you."}
        val g = {a:String -> "Hello, $a"}

        val fog = compose(f, g)

        fog("Robert") shouldBe "Hello, Robert, nice to meet you."
    }
}