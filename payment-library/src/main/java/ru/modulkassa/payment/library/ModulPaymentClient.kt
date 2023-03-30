package ru.modulkassa.payment.library

import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import ru.modulkassa.payment.library.domain.entity.PaymentOptions
import ru.modulkassa.payment.library.domain.entity.result.PaymentResult
import ru.modulkassa.payment.library.ui.PaymentActivityResultContract

/**
 * Клиент для работы с оплатами
 */
class ModulPaymentClient(
    /**
     * Идентификатор магазина, который выдается в личном кабинете на этапе интеграции
     */
    private val merchantId: String,
    /**
     * Ключ для криптографической подписи
     */
    private val signatureKey: String
) {

    private lateinit var startPaymentForResult: ActivityResultLauncher<PaymentOptions>

    /**
     * Зарегистрировать коллбэк на оплату
     * @param activity - ссылка на клиентскую ComponentActivity для вызова [ComponentActivity.registerForActivityResult]
     * @param resultProvider - callback с результатом платежа
     */
    fun registerActivityCallback(activity: ComponentActivity, resultProvider: (result: PaymentResult) -> Unit) {
        startPaymentForResult =
            activity.registerForActivityResult(PaymentActivityResultContract()) { result: PaymentResult ->
                resultProvider.invoke(result)
            }
    }

    /**
     * Провести разовый платеж по СБП
     * @param options - параметры платежа
     */
    fun payBySbp(
        options: PaymentOptions
    ) {
        options.merchantId = merchantId
        options.signatureKey = signatureKey
        startPaymentForResult.launch(options)
    }

    /**
     * Рекурентный платеж по СБП
     */
    // todo cronDelay / interval -> обсудить с сервером формат
    // todo fun recurrentPayBySbp(options: PaymentOptions, cronDelay: String) {}

    // todo getPayments() - какой формат? тоже с UI - активити с диалогом-крутилкой? или поднимать сервис?
}