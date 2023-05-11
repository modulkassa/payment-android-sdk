package ru.modulkassa.payment.library

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import ru.modulkassa.payment.library.domain.entity.PaymentOptions
import ru.modulkassa.payment.library.domain.entity.result.PaymentResult
import ru.modulkassa.payment.library.ui.PaymentActivityCreateActionContract
import ru.modulkassa.payment.library.ui.PaymentActivityResultActionContract

/**
 * Клиент для работы с оплатами
 */
class ModulPaymentClient {

    private lateinit var startPaymentForResult: ActivityResultLauncher<PaymentOptions>

    private lateinit var getPaymentResult: ActivityResultLauncher<String>

    /**
     * Зарегистрировать коллбэк на результат оплаты
     * @param activity - ссылка на клиентскую ComponentActivity для вызова [ComponentActivity.registerForActivityResult]
     * @param resultProvider - callback с результатом платежа
     */
    fun registerPaymentResultCallback(activity: ComponentActivity, resultProvider: (result: PaymentResult) -> Unit) {
        startPaymentForResult =
            activity.registerForActivityResult(PaymentActivityCreateActionContract()) { result: PaymentResult ->
                resultProvider.invoke(result)
            }

        getPaymentResult =
            activity.registerForActivityResult(PaymentActivityResultActionContract()) { result: PaymentResult ->
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
        startPaymentForResult.launch(options)
    }

    /**
     * Получить результат оплаты
     * @param orderId - идентификатор заказа, по которому была оплата
     */
    fun getPaymentResult(
        orderId: String
    ) {
        getPaymentResult.launch(orderId)
    }

    /**
     * Инициализирует клиента пользовательскими данными
     */
    fun init(
        context: Context,
        /**
         * Идентификатор магазина, который выдается в личном кабинете на этапе интеграции
         */
        merchantId: String,
        /**
         * Ключ для криптографической подписи
         */
        signatureKey: String
    ) {
        DefaultSettingsRepository(context)
            .apply {
                save(merchantId, signatureKey)
            }
    }
}