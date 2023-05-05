package ru.modulkassa.payment.library.domain.entity.result

import android.os.Bundle

/**
 * Успешный результат платежа
 */
data class PaymentResultSuccess(
    // todo изменить поля, согласно данным от сервера
    // todo SDK-10 Формат данных для ответа исходя из данных по АПИ
    val transactionId: String
) : PaymentResult() {

    companion object {
        private const val KEY_TRANSACTION_ID = "key_transaction_id"

        fun fromBundle(data: Bundle?): PaymentResultSuccess {
            return if (data == null) {
                PaymentResultSuccess("")
            } else {
                PaymentResultSuccess(data.getString(KEY_TRANSACTION_ID, ""))
            }
        }
    }

    override fun toBundle(): Bundle {
        return Bundle().apply {
            putString(KEY_TRANSACTION_ID, transactionId)
        }
    }
}
