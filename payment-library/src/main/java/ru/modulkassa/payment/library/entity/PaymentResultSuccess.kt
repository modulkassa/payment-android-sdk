package ru.modulkassa.payment.library.entity

import android.os.Bundle

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
