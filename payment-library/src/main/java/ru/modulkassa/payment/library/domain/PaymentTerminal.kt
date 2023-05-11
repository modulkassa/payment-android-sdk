package ru.modulkassa.payment.library.domain

import io.reactivex.rxjava3.core.Single
import ru.modulkassa.payment.library.domain.entity.PaymentOptions
import ru.modulkassa.payment.library.domain.entity.result.PaymentResultSuccess

/**
 * Интерфейс терминала для проведения операции оплаты
 */
internal interface PaymentTerminal {
    /**
     * Получить СБП ссылку для оплаты
     * @param options - параметры оплаты
     * @return СБП ссылку на оплату
     */
    fun createSbpPaymentLink(options: PaymentOptions): Single<String>

    /**
     * Получить результат окончания процесса оплаты
     * @param orderId - идентификатор заказа
     */
    fun getPaymentStatus(orderId: String): Single<PaymentResultSuccess>
}