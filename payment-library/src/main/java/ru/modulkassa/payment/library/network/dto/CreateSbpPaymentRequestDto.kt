package ru.modulkassa.payment.library.network.dto

import com.google.gson.annotations.SerializedName

internal data class CreateSbpPaymentRequestDto(
    /**
     * Идентификатор магазина, который выдается в личном кабинете на этапе интеграции
     */
    val merchant: String,
    /**
     * Сумма платежа
     */
    val amount: String,
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
    val receiptItems: String? = null
) : BaseRequestDto()