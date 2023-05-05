package ru.modulkassa.payment.library.network.dto

import com.google.gson.annotations.SerializedName

internal data class TransactionRequestDto(
    /**
     * Идентификатор магазина, который выдается в личном кабинете на этапе интеграции
     */
    val merchant: String,
    /**
     * Уникальный идентификатор заказа в интернет-магазине
     */
    @SerializedName("order_id")
    val orderId: String,
) : BaseRequestDto()