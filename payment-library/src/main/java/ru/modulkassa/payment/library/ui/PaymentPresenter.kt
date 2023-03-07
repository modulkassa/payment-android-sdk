package ru.modulkassa.payment.library.ui

import ru.modulkassa.payment.library.entity.PaymentOptions

internal class PaymentPresenter(

) : BaseRxPresenter<PaymentView>(), PaymentUserActions {

    override fun checkPaymentOptionsAndShow(options: PaymentOptions) {
        // todo проверить на валидность и полноту данных, когда будет известно, какие данные нужны, показать ошибки
        getView()?.showDescription(options.description)
        val positions = options.inventPositions
        if (positions.isNotEmpty()) {
            getView()?.showPositions(positions)
            val sum = positions.sumOf { it.price.multiply(it.quantity) }
            getView()?.showSum(sum)
        } else {
            getView()?.setErrorResult(NoInventPositionsError())
        }
    }

}