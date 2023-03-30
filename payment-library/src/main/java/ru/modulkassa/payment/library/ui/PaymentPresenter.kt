package ru.modulkassa.payment.library.ui

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.modulkassa.payment.library.domain.PaymentTerminal
import ru.modulkassa.payment.library.domain.entity.PaymentOptions

internal class PaymentPresenter(
    private val paymentTerminal: PaymentTerminal
) : BaseRxPresenter<PaymentView>(), PaymentUserActions {

    override fun checkPaymentOptionsAndShow(options: PaymentOptions) {
        println("Валидируем поля запроса на оплату $options")
        // todo проверить на валидность и полноту данных, когда будет известно, какие данные нужны, показать ошибки
        // PaymentOptionsValidator todo SDK-14 Валидация параметров платежа (табличка в ТЗ)

        getView()?.showDescription(options.description)

        // todo переделаю этот блок в задаче про валидацию данных SDK-14
        val positions = options.positions
        if (positions?.isNotEmpty() == true) {
            getView()?.showPositions(positions)
            getView()?.showSum(options.calculateAmount())
        } else {
            options.amount?.let {
                getView()?.showSum(it)
            }
        }
    }

    override fun payBySbp(options: PaymentOptions) {
        getView()?.showProgress()
        unsubscribeOnDestroy(paymentTerminal
            .createSbpPaymentLink(options)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ sbpLink ->
                getView()?.hideProgress()
                getView()?.sendSbpLink(sbpLink)
            }, { error ->
                // todo SDK-9 Продумать обработку ошибок из апи и rx цепочек
                getView()?.sendSbpLink(error.message ?: "ошибка")
            })
        )
    }

// todo SDK-9 Продумать обработку ошибок из апи и rx цепочек
//    private fun handleError(error: Throwable) {
//        when {
//            (error as? HttpException)?.code() == 400 -> viewState.sendError(R.string.error_400_code)
//            (error as? HttpException)?.code() == 404 -> viewState.sendError(R.string.error_404_code)
//            error is SocketTimeoutException -> viewState.sendError(R.string.error_network_timeout)
//            error is TimeoutException -> viewState.sendError(R.string.error_timeout)
//            error is UnknownHostException || error is SocketException -> {
//                viewState.sendError(R.string.error_network_connection)
//            }
//            else -> getView()?.(error.message)
//        }
//    }

}