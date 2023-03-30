package ru.modulkassa.payment.library.network.dto.position

import com.google.gson.annotations.SerializedName

/**
 * Система налогообложения
 */
internal enum class TaxationModeDto {
    /**
     * Общая СН
     */
    @SerializedName("osn")
    OSN,

    /**
     * Упрощенная СН (доходы)
     */
    @SerializedName("usn_income")
    USN_INCOME,

    /**
     * Упрощенная СН (доходы минус расходы)
     */
    @SerializedName("usn_income_outcome")
    USN_INCOME_OUTCOME,

    /**
     * Единый налог на вмененный доход
     */
    @SerializedName("envd")
    ENVD,

    /**
     * Единый сельскохозяйственный налог
     */
    @SerializedName("esn")
    ESN,

    /**
     * Патентная СН
     */
    @SerializedName("patent")
    PATENT;
}