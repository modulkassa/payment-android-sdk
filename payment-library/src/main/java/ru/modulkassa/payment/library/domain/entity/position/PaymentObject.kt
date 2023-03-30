package ru.modulkassa.payment.library.domain.entity.position

enum class PaymentObject {
    /**
     * Товар
     */
    COMMODITY,

    /**
     * Подакцизный товар
     */
    EXCISE,

    /**
     * Работа
     */
    JOB,

    /**
     * Услуга
     */
    SERVICE,

    /**
     * Ставка азартной игры
     */
    GAMBLING_BET,

    /**
     * Выигрыш азартной игры
     */
    GAMBLING_PRIZE,

    /**
     * Лотерейный билет
     */
    LOTTERY,

    /**
     * Выигрыш лотереи
     */
    LOTTERY_PRIZE,

    /**
     * Предоставление прав на использование результатов интеллектуальной деятельности
     */
    INTELLECTUAL_ACTIVITY,

    /**
     * Платеж
     */
    PAYMENT,

    /**
     * Агентское вознаграждение
     */
    AGENT_COMISSION,

    /**
     * Составной предмет расчета
     */
    COMPOSITE,

    /**
     * Иной предмет расчета
     */
    ANOTHER
}