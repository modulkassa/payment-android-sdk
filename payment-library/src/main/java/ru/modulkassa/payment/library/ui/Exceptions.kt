package ru.modulkassa.payment.library.ui

import androidx.annotation.StringRes

/**
 * Ошибка валидации платежа
 */
internal class ValidationException(
    val causeMessage: String? = null,
    @StringRes
    val causeResource: Int? = null
) : Exception(causeMessage)

/**
 * Ошибка при сетевом запросе
 */
internal class NetworkException(
    val causeMessage: String? = null
) : Exception(causeMessage)

/**
 * Оплата завершилась с ошибкой
 */
internal class PaymentFailedException(
    val causeMessage: String? = null
) : Exception(causeMessage)