package ru.modulkassa.payment.library.ui

import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

/**
 * Интерфейс для форматирования сумм
 * Формирует число-строку с количеством символов после запятой [RubSuffixSumFormatter.SUM_SCALE]
 * с форматом символов форматирования [RubSuffixSumFormatter.formatSymbols]
 * после суммы добавляет значок рубля [RubSuffixSumFormatter.RUB_SIGN]
 */
class RubSuffixSumFormatter {

    companion object {
        private const val SUM_SCALE = 2
        private const val RUB_SIGN = "\u20BD"
    }

    private val decimalFormat = DecimalFormat()
    private val formatSymbols = DecimalFormatSymbols()

    init {
        formatSymbols.decimalSeparator = '.'
        formatSymbols.groupingSeparator = ' '
        decimalFormat.maximumFractionDigits = SUM_SCALE
        decimalFormat.minimumFractionDigits = SUM_SCALE
        decimalFormat.decimalFormatSymbols = formatSymbols
        decimalFormat.groupingSize = 3
    }

    fun format(sum: BigDecimal): String {
        return decimalFormat.format(sum.setScale(SUM_SCALE, RoundingMode.HALF_UP)).plus(" $RUB_SIGN")
    }
}