package ru.modulkassa.payment.library.network

import com.google.common.truth.Truth
import org.junit.Test
import java.math.BigDecimal

class BigDecimalFormatterTest {

    @Test
    fun Format_ZeroFractional_Formatted() {
        val result = BigDecimalFormatter.format(BigDecimal("123.00"))

        Truth.assertThat(result).isEqualTo("123")
    }

    @Test
    fun Format_ExtraZeroFractional_Formatted() {
        val result = BigDecimalFormatter.format(BigDecimal("123.10"))

        Truth.assertThat(result).isEqualTo("123.1")
    }

    @Test
    fun Format_WithRound_Formatted() {
        val result = BigDecimalFormatter.format(BigDecimal("123.1256"))

        Truth.assertThat(result).isEqualTo("123.13")
    }

    @Test
    fun Format_ByDefault_Formatted() {
        val result = BigDecimalFormatter.format(BigDecimal("123.12"))

        Truth.assertThat(result).isEqualTo("123.12")
    }

    @Test
    fun Format_OneHundred_Formatted() {
        val result = BigDecimalFormatter.format(BigDecimal("100"))

        Truth.assertThat(result).isEqualTo("100")
    }

    @Test
    fun Format_TreeScale_Formatted() {
        val result = BigDecimalFormatter.format(BigDecimal("100.12345"), scale = 3)

        Truth.assertThat(result).isEqualTo("100.123")
    }

}