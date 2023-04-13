package ru.modulkassa.payment.library.ui

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.modulkassa.payment.library.domain.PaymentOptionsValidator
import ru.modulkassa.payment.library.domain.PaymentTerminal
import ru.modulkassa.payment.library.domain.entity.PaymentOptions

internal class PaymentPresenter(
    private val paymentTerminal: PaymentTerminal
) : BaseRxPresenter<PaymentView>(), PaymentUserActions {

    override fun checkPaymentOptionsAndShow(options: PaymentOptions) {
        Completable.fromAction {
            PaymentOptionsValidator.validate(options)
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                getView()?.showDescription(options.description)
                val positions = options.positions
                if (positions?.isNotEmpty() == true) {
                    getView()?.showPositions(positions)
                    getView()?.showSum(options.calculateAmount())
                } else {
                    options.amount?.let {
                        getView()?.showSum(it)
                    }
                }
            }, { exception ->
                val errorResult = if (exception is ValidationException) {
                    ValidationErrorResult(cause = exception.causeMessage, causeResource = exception.causeResource)
                } else {
                    UnknownErrorResult()
                }
                getView()?.setErrorResult(errorResult)
            })
    }

    override fun payBySbp(options: PaymentOptions) {
        getView()?.showProgress()
        unsubscribeOnDestroy(
            paymentTerminal
                .createSbpPaymentLink(options)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ sbpLink ->
                    getView()?.hideProgress()
                    getView()?.sendSbpLink(sbpLink)
                }, { error ->
                    when (error) {
                        is ValidationException -> getView()?.setErrorResult(
                            ValidationErrorResult(
                                cause = error.causeMessage,
                                causeResource = error.causeResource
                            )
                        )
                        else -> getView()?.showErrorScreen()
                    }
                })
        )
    }

}