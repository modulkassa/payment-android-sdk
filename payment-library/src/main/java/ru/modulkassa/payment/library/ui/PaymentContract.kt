package ru.modulkassa.payment.library.ui

import androidx.annotation.StringRes
import ru.modulkassa.payment.library.domain.entity.PaymentOptions
import ru.modulkassa.payment.library.domain.entity.position.Position
import ru.modulkassa.payment.library.domain.entity.result.PaymentResult
import ru.modulkassa.payment.library.domain.entity.result.PaymentResultSuccess
import java.math.BigDecimal

internal interface PaymentView : BaseView {

    /**
     * Закрыть экран с ошибкой
     */
    fun setErrorResult(error: BaseErrorResult)

    /**
     * Закрыть экран с успехом
     */
    fun setSuccessResult(result: PaymentResultSuccess)

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
    fun showProgress(@StringRes progressResource: Int)

    /**
     * Скрыть прогресс
     */
    fun hideProgress()

    /**
     * Послать СБП ссылку установленным приложениям
     */
    fun sendSbpLink(sbpLink: String)

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

    /**
     * Дождаться результат оплаты
     */
    fun getPaymentResult(options: PaymentOptions)
}