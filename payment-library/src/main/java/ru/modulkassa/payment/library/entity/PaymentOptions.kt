package ru.modulkassa.payment.library.entity

import android.os.Bundle
import com.google.gson.Gson

/**
 * Параметры платежа
 */
data class PaymentOptions(
    // todo id нужен?
    /**
     * Описание платежа
     */
    var description: String,
    /**
     * Список позиций для платежа
     */
    var inventPositions: List<InventPosition>
): Bundable {

    companion object {
        const val KEY_SERIALIZED_PAYMENT_OPTIONS = "key_serialized_payment_options"

        fun fromBundle(data: Bundle): PaymentOptions? {
            return GsonFactory.provide().fromJson(data.getString(KEY_SERIALIZED_PAYMENT_OPTIONS), PaymentOptions::class.java)
        }
    }

    override fun toBundle(): Bundle {
        return Bundle().also {
            val serializedPaymentOptions = GsonFactory.provide().toJson(this)
            it.putString(KEY_SERIALIZED_PAYMENT_OPTIONS, serializedPaymentOptions)
        }
    }

}