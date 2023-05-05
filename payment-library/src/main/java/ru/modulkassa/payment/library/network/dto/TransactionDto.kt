package ru.modulkassa.payment.library.network.dto

import com.google.gson.annotations.SerializedName

internal data class TransactionDto(
    @SerializedName("transaction_id")
    val transactionId: String? = null,
    val state: TransactionStateDto? = null,
    @SerializedName("payment_method")
    val paymentMethod: String? = null,
    /**
     * Сумма с учетом возвратов
     */
    val amount: String? = null,
    /**
     * Изначальная сумма транзакции
     */
    @SerializedName("original_amount")
    val originalAmount: String? = null,
    val currency: String? = null,
    @SerializedName("pan_mask")
    val panMask: String? = null,
    val rrn: String? = null,
    @SerializedName("auth_code")
    val authCode: String? = null,
    @SerializedName("auth_number")
    val authNumber: String? = null,
    val message: String? = null
)

internal enum class TransactionStateDto {
    /**
     * В процессе
     */
    PROCESSING,

    /**
     * Ожидает 3DS
     */
    WAITING_FOR_3DS,

    /**
     * Транзакция завершена успешно
     */
    COMPLETE,

    /**
     * Транзакция завершена с ошибкой
     */
    FAILED
}