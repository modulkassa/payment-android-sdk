package ru.modulkassa.payment.library.ui

import android.content.Context
import androidx.annotation.StringRes
import ru.modulkassa.payment.library.R
import ru.modulkassa.payment.library.domain.entity.result.ErrorType
import ru.modulkassa.payment.library.domain.entity.result.PaymentResultError

internal sealed class BaseErrorResult(
    @StringRes
    private val stringResource: Int = R.string.error_unknown,
    private val type: ErrorType = ErrorType.UNKNOWN
) {
    fun toPaymentResultError(context: Context): PaymentResultError {
        return PaymentResultError(
            message = context.getString(this.stringResource),
            type = this.type
        )
    }
}

/**
 * Не указаны параметры оплаты
 */
internal class NoPaymentOptionsError : BaseErrorResult(R.string.error_no_payment_options, ErrorType.INVALID_DATA)

/**
 * Отменено пользователем
 */
internal class CanceledByUserError : BaseErrorResult(R.string.error_cancelled_by_user, ErrorType.CANCELLED)