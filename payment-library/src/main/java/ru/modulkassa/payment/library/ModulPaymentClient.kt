package ru.modulkassa.payment.library

import ru.modulkassa.payment.library.ui.PaymentActivityResultContract

// todo Общие вопросы на обсудить когда-нибудь
// todo будем ли собирать логи и отправлять в лог-коллектор, чтобы разбираться с инц от пользователей?

/**
 * Клиент для работы с оплатами
 */
class ModulPaymentClient(
    // todo пробросить ключи/токены для работы с сервером
) {

    /**
     * Разовый платеж по СБП
     * todo описать конечно поподробнее надо
     * todo подумать над формой ответа
     */
    fun createSbpPaymentContract(): PaymentActivityResultContract {
        return PaymentActivityResultContract()
    }

    /**
     * Рекурентный платеж по СБП
     */
    // todo cronDelay / interval -> обсудить с сервером формат
    // todo fun recurrentPayBySbp(options: PaymentOptions, cronDelay: String) {}

    // todo getPayments() - какой формат? тоже с UI - активити с диалогом-крутилкой? или поднимать сервис?
}