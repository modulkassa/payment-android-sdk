package ru.modulkassa.payment.library.ui

import android.content.Intent
import ru.modulkassa.payment.library.entity.InventPosition
import ru.modulkassa.payment.library.entity.PaymentOptions


internal interface PaymentView : BaseView {

    /**
     * Закрыть экран с ошибкой
     */
    fun setErrorResult(error: Throwable)

    /**
     * Отобразить описание платежа
     */
    fun showDescription(description: String)

    /**
     * Отобразить позиции платежа
     */
    fun showPositions(positions: List<InventPosition>)

}

internal interface PaymentUserActions : BaseUserActions<PaymentView> {

    /**
     * Проверить параметры оплаты на валидность и отобразить на экране
     */
    fun checkPaymentOptionsAndShow(options: PaymentOptions)
}