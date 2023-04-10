package ru.modulkassa.payment.library.ui

import ru.modulkassa.payment.library.domain.entity.PaymentOptions
import ru.modulkassa.payment.library.domain.entity.position.Position
import java.math.BigDecimal

internal interface PaymentView : BaseView {

    /**
     * Закрыть экран с ошибкой
     */
    fun setErrorResult(error: BaseErrorResult)

    /**
     * Отобразить описание платежа
     */
    fun showDescription(description: String)

    /**
     * Отобразить позиции платежа
     */
    fun showPositions(positions: List<Position>)

    /**
     * Отобразить сумму платежа
     */
    fun showSum(sum: BigDecimal)

    /**
     * Показать прогресс
     */
    fun showProgress()

    /**
     * Скрыть прогресс
     */
    fun hideProgress()

    /**
     * Послать СБП ссылку установленным приложениям
     */
    fun sendSbpLink(sbpLink: String)

    /**
     * Отобразить экран с ошибкой
     */
    fun showErrorScreen()

}

internal interface PaymentUserActions : BaseUserActions<PaymentView> {

    /**
     * Проверить параметры оплаты на валидность и отобразить на экране
     */
    fun checkPaymentOptionsAndShow(options: PaymentOptions)

    /**
     * Провести оплату по СБП
     */
    fun payBySbp(options: PaymentOptions)
}