package ru.modulkassa.payment.library.ui

import android.content.Context
import androidx.annotation.StringRes
import ru.modulkassa.payment.library.R
import ru.modulkassa.payment.library.domain.entity.result.ErrorType
import ru.modulkassa.payment.library.domain.entity.result.PaymentResultError

internal open class BaseErrorResult(
    @StringRes
    private val stringResource: Int = R.string.error_unknown,
    private val type: ErrorType = ErrorType.UNKNOWN,
    private val cause: String? = null,
    @StringRes
    private val causeResource: Int? = null
) {
    fun toPaymentResultError(context: Context): PaymentResultError {
        return PaymentResultError(
            message = context.getString(this.stringResource),
            type = this.type,
            cause = cause ?: this.causeResource?.let { context.getString(it) } ?: ""
        )
    }
}

/**
 * Не указаны параметры оплаты
 */
internal class NoPaymentOptionsErrorResult :
    BaseErrorResult(R.string.error_result_no_payment_options, ErrorType.INVALID_DATA)

/**
 * Отменено пользователем
 */
internal class CanceledByUserErrorResult : BaseErrorResult(R.string.error_result_cancelled_by_user, ErrorType.CANCELLED)

/**
 * Ошибка валидации данных при создании платежа
 */
internal class ValidationErrorResult(
    cause: String? = null,
    causeResource: Int? = null
) : BaseErrorResult(R.string.error_result_validation, ErrorType.INVALID_DATA, cause, causeResource)

/**
 * Нет доступного приложения для оплаты
 */
internal class NoPaymentAppErrorResult : BaseErrorResult(R.string.error_result_no_payment_app, ErrorType.NO_PAYMENT_APP)