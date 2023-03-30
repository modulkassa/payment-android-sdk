package ru.modulkassa.payment.library.domain.entity.position

enum class VatTag {
    /**
     * Без НДС
     */
    NONE,

    /**
     * НДС чека по ставке 0%
     */
    VAT_0,

    /**
     * НДС чека по ставке 10%
     */
    VAT_10,

    /**
     * НДС чека по ставке 20%
     */
    VAT_20,

    /**
     * НДС чека по расчетной ставке 10%
     */
    VAT_110,

    /**
     * НДС чека по расчетной ставке 20%
     */
    VAT_120
}