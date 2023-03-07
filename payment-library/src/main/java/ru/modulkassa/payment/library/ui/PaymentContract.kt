package ru.modulkassa.payment.library.ui

import ru.modulkassa.payment.library.entity.InventPosition
import ru.modulkassa.payment.library.entity.PaymentOptions
import java.math.BigDecimal


internal interface PaymentView : BaseView {

    /**
     * Закрыть экран с ошибкой
     */
    fun setErrorResult(error: BaseError)

    /**
     * Отобразить описание платежа
     */
    fun showDescription(description: String)

    /**
     * Отобразить позиции платежа
     */
    fun showPositions(positions: List<InventPosition>)

    /**
     * Отобразить сумму платежа
     */
    fun showSum(sum: BigDecimal)

}

internal interface PaymentUserActions : BaseUserActions<PaymentView> {

    /**
     * Проверить параметры оплаты на валидность и отобразить на экране
     */
    fun checkPaymentOptionsAndShow(options: PaymentOptions)
}