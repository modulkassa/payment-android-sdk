package ru.modulkassa.payment.library.network.dto.position

import com.google.gson.annotations.SerializedName

internal enum class PaymentMethodDto {
    /**
     * Предоплата 100%
     */
    @SerializedName("full_prepayment")
    FULL_PREPAYMENT,

    /**
     * Предоплата
     */
    @SerializedName("prepayment")
    PREPAYMENT,

    /**
     * Аванс
     */
    @SerializedName("advance")
    ADVANCE,

    /**
     * Полный расчет
     */
    @SerializedName("full_payment")
    FULL_PAYMENT,

    /**
     * Частичный расчет и кредит
     */
    @SerializedName("partial_payment")
    PARTIAL_PAYMENT,

    /**
     * Передача в кредит
     */
    @SerializedName("credit")
    CREDIT,

    /**
     * Оплата кредита
     */
    @SerializedName("credit_payment")
    CREDIT_PAYMENT
}