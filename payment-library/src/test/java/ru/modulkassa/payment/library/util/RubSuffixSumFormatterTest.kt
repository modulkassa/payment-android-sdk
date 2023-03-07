package ru.modulkassa.payment.library.util

import com.google.common.truth.Truth
import org.junit.Test
import ru.modulkassa.payment.library.ui.RubSuffixSumFormatter
import java.math.BigDecimal

class RubSuffixSumFormatterTest {

    @Test
    fun Format_AddRouble_StringWithRouble() {
        val formatter = RubSuffixSumFormatter()

        val result = formatter.format(BigDecimal("123.5"))

        Truth.assertThat(result).isEqualTo("123.50 \u20BD")
    }

    @Test
    fun Format_WithExtraScale_CutsWithSeparator() {
        val formatter = RubSuffixSumFormatter()

        val result = formatter.format(BigDecimal("123.127"))

        Truth.assertThat(result).isEqualTo("123.13 ₽")
    }

    @Test
    fun Format_WithGroup_SetsGroups() {
        val formatter = RubSuffixSumFormatter()

        val result = formatter.format(BigDecimal("999888777.91"))

        Truth.assertThat(result).isEqualTo("999 888 777.91 ₽")
    }

}