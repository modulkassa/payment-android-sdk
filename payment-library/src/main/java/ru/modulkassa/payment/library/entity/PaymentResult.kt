package ru.modulkassa.payment.library.entity

import android.os.Bundle
import java.math.BigDecimal

/**
 * Результат платежа
 */
sealed class PaymentResult : Bundable

/**
 * Ошибочный результат платежа
 */
data class PaymentResultError(
    // todo изменить поля, согласно данным от сервера
    /**
     * Описание ошибки
     */
    val message: String
) : PaymentResult() {

    companion object {
        private const val KEY_ERROR_MESSAGE = "key_error_message"

        fun fromBundle(data: Bundle?): PaymentResultError {
            return if (data == null) {
                PaymentResultError("Запрос был отменен")
            } else {
                PaymentResultError(data.getString(KEY_ERROR_MESSAGE, "Произошла неизвестная ошибка"))
            }
        }
    }

    override fun toBundle(): Bundle {
        return Bundle().apply {
            putString(KEY_ERROR_MESSAGE, message)
        }
    }
}

/**
 * Успешный результат платежа
 */
data class PaymentResultSuccess(
    // todo изменить поля, согласно данным от сервера
    val paymentId: String
) : PaymentResult() {

    companion object {
        private const val KEY_PAYMENT_ID = "key_payment_id"

        fun fromBundle(data: Bundle?): PaymentResultSuccess {
            return if (data == null) {
                PaymentResultSuccess("")
            } else {
                PaymentResultSuccess(data.getString(KEY_PAYMENT_ID, ""))
            }
        }
    }

    override fun toBundle(): Bundle {
        return Bundle().apply {
            putString(KEY_PAYMENT_ID, paymentId)
        }
    }
}
