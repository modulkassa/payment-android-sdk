package ru.modulkassa.payment.library.ui

import android.content.Context
import androidx.annotation.StringRes
import ru.modulkassa.payment.library.R
import ru.modulkassa.payment.library.domain.entity.result.ErrorType
import ru.modulkassa.payment.library.domain.entity.result.PaymentResultError

internal open class BaseErrorResult(
    @StringRes
    private val stringResource: Int = R.string.error_unknown,
    private val type: ErrorType = ErrorType.FAILED,
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
 * Отменено пользователем
 */
internal class CanceledByUserErrorResult : BaseErrorResult(R.string.error_result_cancelled_by_user, ErrorType.CANCELLED)

/**
 * Не указаны параметры оплаты
 */
internal class NoPaymentOptionsErrorResult :
    BaseErrorResult(R.string.error_result_no_payment_options, ErrorType.INVALID_DATA)

/**
 * Не указан идентификатор заказа
 */
internal class NoOrderIdErrorResult :
    BaseErrorResult(R.string.error_result_no_order_id, ErrorType.INVALID_DATA)

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
internal class NoPaymentAppErrorResult : BaseErrorResult(R.string.error_result_no_payment_app, ErrorType.FAILED)

/**
 * Сетевая ошибка выполнения запроса
 */
internal class NetworkErrorResult(
    cause: String? = null
) : BaseErrorResult(R.string.error_result_network, ErrorType.FAILED, cause)

/**
 * Оплата не прошла
 */
internal class PaymentFailedErrorResult(
    cause: String? = null
) : BaseErrorResult(R.string.error_result_payment_failed, ErrorType.FAILED, cause)

/**
 * Результат оплаты неизвестен
 */
internal class TimeoutErrorResult : BaseErrorResult(R.string.error_result_timeout, ErrorType.FAILED)