package ru.modulkassa.payment.library.network.dto.position

import com.google.gson.annotations.SerializedName

internal enum class VatTagDto {
    /**
     * Без НДС
     */
    @SerializedName("none")
    NONE,

    /**
     * НДС чека по ставке 0%
     */
    @SerializedName("vat0")
    VAT_0,

    /**
     * НДС чека по ставке 10%
     */
    @SerializedName("vat10")
    VAT_10,

    /**
     * НДС чека по ставке 20%
     */
    @SerializedName("vat20")
    VAT_20,

    /**
     * НДС чека по расчетной ставке 10%
     */
    @SerializedName("vat110")
    VAT_110,

    /**
     * НДС чека по расчетной ставке 20%
     */
    @SerializedName("vat120")
    VAT_120
}