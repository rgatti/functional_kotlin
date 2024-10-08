package functional.kotlin.ch2

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class `ex2-3` {

    @Test
    fun `curry test`() {
        val f = {i:Int, j:Int -> i + j }

        val `f'` = curry(f)(1)

        `f'`(2) shouldBe 3
    }
}