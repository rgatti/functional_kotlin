package functional.kotlin.ch2

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.junit.jupiter.params.provider.ValueSource
import java.util.stream.Stream
import kotlin.math.exp

class `ex2-1` {

    companion object {
        @JvmStatic
        fun fibValues(): Stream<Arguments> = Stream.of(
            Arguments.of(0, 0),
            Arguments.of(1, 1),
            Arguments.of(2, 1),
            Arguments.of(3, 2),
            Arguments.of(4, 3),
            Arguments.of(5, 5),
            Arguments.of(6, 8),
            Arguments.of(7, 13),
            Arguments.of(8, 21),
            Arguments.of(9, 34),
            Arguments.of(10, 55),
            Arguments.of(25, 75025),
            Arguments.of(50, 12586269025),
        )
    }

    @ParameterizedTest
    @MethodSource("fibValues")
    fun `test fib`(i: Int, expected: Long) {
        fib(i) shouldBe expected
    }
}