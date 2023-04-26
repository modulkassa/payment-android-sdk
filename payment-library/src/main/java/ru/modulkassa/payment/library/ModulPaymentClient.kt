package ru.modulkassa.payment.library

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import ru.modulkassa.payment.library.domain.entity.PaymentOptions
import ru.modulkassa.payment.library.domain.entity.result.PaymentResult
import ru.modulkassa.payment.library.ui.PaymentActivityResultContract

/**
 * Клиент для работы с оплатами
 */
class ModulPaymentClient {

    private lateinit var startPaymentForResult: ActivityResultLauncher<PaymentOptions>

    /**
     * Зарегистрировать коллбэк на оплату
     * @param activity - ссылка на клиентскую ComponentActivity для вызова [ComponentActivity.registerForActivityResult]
     * @param resultProvider - callback с результатом платежа
     */
    fun registerPaymentCallback(activity: ComponentActivity, resultProvider: (result: PaymentResult) -> Unit) {
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
        startPaymentForResult.launch(options)
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