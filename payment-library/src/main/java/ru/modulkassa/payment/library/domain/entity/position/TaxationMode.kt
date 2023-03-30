package ru.modulkassa.payment.library.domain.entity.position

/**
 * Система налогообложения
 */
enum class TaxationMode {
    /**
     * Общая СН
     */
    OSN,

    /**
     * Упрощенная СН (доходы)
     */
    USN_INCOME,

    /**
     * Упрощенная СН (доходы минус расходы)
     */
    USN_INCOME_OUTCOME,

    /**
     * Единый налог на вмененный доход
     */
    ENVD,

    /**
     * Единый сельскохозяйственный налог
     */
    ESN,

    /**
     * Патентная СН
     */
    PATENT;
}