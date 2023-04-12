package ru.modulkassa.payment.library.domain.entity.position

enum class PositionType {
    /**
     * Товар
     */
    COMMODITY,

    /**
     * Работа
     */
    JOB,

    /**
     * Услуга
     */
    SERVICE,

    /**
     * Платеж
     */
    PAYMENT,

    /**
     * Иной предмет расчета
     */
    ANOTHER
}