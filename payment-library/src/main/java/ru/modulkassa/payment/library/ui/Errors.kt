package ru.modulkassa.payment.library.ui

import android.content.Context
import androidx.annotation.StringRes
import ru.modulkassa.payment.library.R
import ru.modulkassa.payment.library.entity.ErrorType
import ru.modulkassa.payment.library.entity.PaymentResultError

internal sealed class BaseError(
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
internal class NoPaymentOptionsError : BaseError(R.string.error_no_payment_options, ErrorType.INVALID_DATA)

/**
 * Список позиций не должен быть пустым
 */
internal class NoInventPositionsError : BaseError(R.string.error_no_invent_positions, ErrorType.INVALID_DATA)

/**
 * Отменено пользователем
 */
internal class CanceledByUserError : BaseError(R.string.error_cancelled_by_user, ErrorType.CANCELLED)