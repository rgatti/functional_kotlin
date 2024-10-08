package functional.kotlin.ch2

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class `ex2-4` {
    @Test
    fun `uncurry test`() {
        val f = { a:Int -> { b:Int -> a + b } }

        val `f'` = uncurry(f)

        `f'`(1, 2) shouldBe 3
    }
}