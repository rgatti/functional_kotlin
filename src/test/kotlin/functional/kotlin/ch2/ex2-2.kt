package functional.kotlin.ch2

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class `ex2-2` {
    companion object {
        @JvmStatic
        fun inputs() = Stream.of(
            Arguments.of(emptyList<Int>(), true),
            Arguments.of(listOf(1),true),
            Arguments.of(listOf(1,2),true),
            Arguments.of(listOf(2,1),false),
            Arguments.of(listOf(1,2,3,4),true),
            Arguments.of(listOf(1,3,2,4),false),
        )
    }

    @ParameterizedTest
    @MethodSource("inputs")
    fun `sorted test`(input:List<Int>, expected: Boolean) {
        isSorted(input) {l,r -> l <= r} shouldBe expected
    }
}