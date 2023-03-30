package ru.modulkassa.payment.library.network.dto

import com.google.gson.annotations.SerializedName
import ru.modulkassa.payment.library.network.dto.position.PositionDto
import java.math.BigDecimal
import java.util.UUID

internal data class CreateSbpPaymentRequestDto(
    /**
     * Идентификатор магазина, который выдается в личном кабинете на этапе интеграции
     */
    val merchant: String,
    /**
     * Сумма платежа
     */
    val amount: BigDecimal,
    /**
     * Уникальный идентификатор заказа в интернет-магазине
     */
    @SerializedName("order_id")
    val orderId: String,
    /**
     * Описание платежа
     */
    val description: String,
    /**
     * Еmail получателя чека
     */
    @SerializedName("receipt_contact")
    val receiptContact: String? = null,
    /**
     * Позиции чека
     */
    @SerializedName("receipt_items")
    val receiptItems: List<PositionDto>? = null,
    /**
     * Криптографическая подпись
     */
    val signature: String
) {
    /**
     * Текущее дата и время
     */
    @SerializedName("unix_timestamp")
    val unixTimestamp: String = (System.currentTimeMillis() / 1000).toString()

    /**
     * Случайная величина
     * Строка (максимум 32 символа), допускаются только печатные ASCII символы
     */
    val salt: String = UUID.randomUUID().toString().take(32)
}