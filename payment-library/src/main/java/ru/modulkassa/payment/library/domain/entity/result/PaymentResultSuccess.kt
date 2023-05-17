package ru.modulkassa.payment.library.domain.entity.result

import android.os.Bundle

/**
 * Успешный результат платежа
 */
data class PaymentResultSuccess(
    val transactionId: String,
    val sbpTransactionId: String,
) : PaymentResult() {

    companion object {
        private const val KEY_TRANSACTION_ID = "key_transaction_id"
        private const val KEY_SBP_TRANSACTION_ID = "key_sbp_transaction_id"

        fun fromBundle(data: Bundle?): PaymentResultSuccess {
            return if (data == null) {
                PaymentResultSuccess("", "")
            } else {
                PaymentResultSuccess(
                    data.getString(KEY_TRANSACTION_ID, ""),
                    data.getString(KEY_SBP_TRANSACTION_ID, ""),
                )
            }
        }
    }

    override fun toBundle(): Bundle {
        return Bundle().apply {
            putString(KEY_TRANSACTION_ID, transactionId)
            putString(KEY_SBP_TRANSACTION_ID, sbpTransactionId)
        }
    }
}
