package ru.modulkassa.payment.library.domain.entity.position

enum class PaymentMethod {
    /**
     * Предоплата 100%
     */
    FULL_PREPAYMENT,

    /**
     * Предоплата
     */
    PREPAYMENT,

    /**
     * Аванс
     */
    ADVANCE,

    /**
     * Полный расчет
     */
    FULL_PAYMENT,

    /**
     * Частичный расчет и кредит
     */
    PARTIAL_PAYMENT,

    /**
     * Передача в кредит
     */
    CREDIT,

    /**
     * Оплата кредита
     */
    CREDIT_PAYMENT
}