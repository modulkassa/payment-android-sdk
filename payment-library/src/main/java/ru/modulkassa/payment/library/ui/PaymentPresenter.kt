package ru.modulkassa.payment.library.ui

import android.content.Intent
import ru.modulkassa.payment.library.entity.PaymentOptions

internal class PaymentPresenter(

) : BaseRxPresenter<PaymentView>(), PaymentUserActions {

    override fun checkPaymentOptionsAndShow(options: PaymentOptions) {
        // todo проверить на валидность и полноту данных, когда будет известно, какие данные нужны, показать ошибки
        getView()?.showDescription(options.description)
        val positions = options.inventPositions
        if (positions.isNotEmpty()) {
            getView()?.showPositions(positions)
        }
    }

}