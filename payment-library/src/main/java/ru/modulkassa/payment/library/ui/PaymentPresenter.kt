package ru.modulkassa.payment.library.ui

import androidx.annotation.StringRes
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.modulkassa.payment.library.R
import ru.modulkassa.payment.library.domain.PaymentOptionsValidator
import ru.modulkassa.payment.library.domain.PaymentTerminal
import ru.modulkassa.payment.library.domain.entity.PaymentOptions
import ru.modulkassa.payment.library.domain.entity.result.ErrorType
import ru.modulkassa.payment.library.domain.entity.result.PaymentResultSuccess

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
            }, { error ->
                handleError(error, R.string.error_unknown_validation)
            })
    }

    override fun payBySbp(options: PaymentOptions) {
        getView()?.showProgress(R.string.create_payment_progress)
        unsubscribeOnDestroy(
            paymentTerminal
                .createSbpPaymentLink(options)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ sbpLink ->
                    getView()?.hideProgress()
                    getView()?.sendSbpLink(sbpLink)
                }, { error ->
                    handleError(error, R.string.error_unknown_create_payment)
                })
        )
    }

    override fun getPaymentResult(options: PaymentOptions) {
        getView()?.showProgress(R.string.check_payment_progress)
        unsubscribeOnDestroy(
            paymentTerminal
                .getPaymentStatus(options)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    if (result.isSuccess) {
                        getView()?.setSuccessResult(PaymentResultSuccess(result.transactionId ?: ""))
                    } else {
                        getView()?.setErrorResult(
                            BaseErrorResult(
                                stringResource = R.string.error_unknown_payment_result,
                                type = ErrorType.UNKNOWN_PAYMENT_RESULT
                            )
                        )
                    }
                }, { error ->
                    handleError(error, R.string.error_unknown_payment_result)
                })
        )
    }

    private fun handleError(exception: Throwable, @StringRes defaultResource: Int) {
        val errorResult = when (exception) {
            is ValidationException -> ValidationErrorResult(
                cause = exception.causeMessage,
                causeResource = exception.causeResource
            )
            else -> BaseErrorResult(
                stringResource = defaultResource,
                cause = exception.message ?: exception.stackTraceToString()
            )
        }
        getView()?.setErrorResult(errorResult)
    }
}