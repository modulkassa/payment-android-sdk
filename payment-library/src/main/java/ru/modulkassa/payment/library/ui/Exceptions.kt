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