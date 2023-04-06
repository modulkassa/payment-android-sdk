package ru.modulkassa.payment.library.network

import java.math.BigDecimal
import java.math.RoundingMode

internal object BigDecimalFormatter {

    fun format(source: BigDecimal, scale: Int = 2): String {
        return source.setScale(scale, RoundingMode.HALF_UP)
            .stripTrailingZeros()
            .toPlainString()
    }
}