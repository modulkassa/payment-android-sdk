package ru.modulkassa.payment.library.domain.entity

import android.os.Bundle
import ru.modulkassa.payment.library.domain.entity.position.Position
import ru.modulkassa.payment.library.network.GsonFactory
import java.math.BigDecimal
import java.math.RoundingMode

/**
 * Параметры платежа
 */
class PaymentOptions private constructor(
    val orderId: String,
    val description: String,
    val amount: BigDecimal? = null,
    val positions: List<Position>? = null,
    val receiptContact: String? = null
) : Bundable {

    companion object {
        const val KEY_SERIALIZED_PAYMENT_OPTIONS = "key_serialized_payment_options"

        fun fromBundle(data: Bundle): PaymentOptions? {
            return GsonFactory.provide()
                .fromJson(data.getString(KEY_SERIALIZED_PAYMENT_OPTIONS), PaymentOptions::class.java)
        }

        /**
         * Заполнение параметров платежа для разового СБП платежа
         */
        fun createSbpOptions(
            /**
             * Уникальный идентификатор заказа в интернет-магазине
             */
            orderId: String,
            /**
             * Описание платежа
             */
            description: String,
            /**
             * Сумма платежа
             * Необязательно, если переданы позиции для платежа
             */
            amount: BigDecimal? = null,
            /**
             * Список позиций для платежа
             */
            positions: List<Position>? = null,
            /**
             * Еmail получателя чека
             * Необязательное
             */
            receiptContact: String? = null
        ): PaymentOptions {
            return PaymentOptions(
                orderId = orderId,
                description = description,
                amount = amount,
                positions = positions,
                receiptContact = receiptContact
            )
        }
    }

    override fun toBundle(): Bundle {
        return Bundle().also {
            val serializedPaymentOptions = GsonFactory.provide().toJson(this)
            it.putString(KEY_SERIALIZED_PAYMENT_OPTIONS, serializedPaymentOptions)
        }
    }

    internal fun calculateAmount(): BigDecimal {
        return positions?.let { positions ->
            positions.sumOf {
                it.price
                    .multiply(it.quantity)
                    .setScale(2, RoundingMode.HALF_UP)
            }
        } ?: BigDecimal.ZERO
    }
}