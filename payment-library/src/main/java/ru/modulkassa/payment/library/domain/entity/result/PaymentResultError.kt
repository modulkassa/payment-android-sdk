package ru.modulkassa.payment.library.domain.entity.result

import android.os.Bundle
import ru.modulkassa.payment.library.domain.entity.result.ErrorType.FAILED

/**
 * Типы ошибок
 */
enum class ErrorType {
    /**
     * Данные запроса невалидны
     */
    INVALID_DATA,

    /**
     * Операция была отменена пользователем
     */
    CANCELLED,

    /**
     * Операция завершилась с ошибкой
     */
    FAILED
}

/**
 * Ошибочный результат платежа
 */
data class PaymentResultError(
    /**
     * Описание ошибки
     */
    val message: String,
    /**
     * Тип ошибки
     */
    val type: ErrorType,
    /**
     * Причина ошибки
     */
    val cause: String = ""
) : PaymentResult() {

    companion object {
        private const val KEY_ERROR_MESSAGE = "key_error_message"
        private const val KEY_ERROR_TYPE = "key_error_type"
        private const val KEY_ERROR_CAUSE = "key_error_cause"

        private const val DEFAULT_MESSAGE = "Произошла неизвестная ошибка"

        fun fromBundle(data: Bundle?): PaymentResultError {
            return if (data == null) {
                PaymentResultError(DEFAULT_MESSAGE, FAILED)
            } else {
                val type = try {
                    ErrorType.valueOf(data.getString(KEY_ERROR_TYPE, ""))
                } catch (error: IllegalArgumentException) {
                    FAILED
                }
                PaymentResultError(
                    message = data.getString(KEY_ERROR_MESSAGE, DEFAULT_MESSAGE),
                    type = type,
                    cause = data.getString(KEY_ERROR_CAUSE, "")
                )
            }
        }
    }

    override fun toBundle(): Bundle {
        return Bundle().apply {
            putString(KEY_ERROR_MESSAGE, message)
            putString(KEY_ERROR_TYPE, type.toString())
            putString(KEY_ERROR_CAUSE, cause)
        }
    }
}