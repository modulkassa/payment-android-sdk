package ru.modulkassa.payment.library.domain

import io.reactivex.rxjava3.core.Single
import ru.modulkassa.payment.library.domain.entity.PaymentOptions

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

}