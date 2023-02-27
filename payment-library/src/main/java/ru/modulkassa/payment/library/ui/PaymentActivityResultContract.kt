package ru.modulkassa.payment.library.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import ru.modulkassa.payment.library.entity.PaymentOptions
import ru.modulkassa.payment.library.entity.PaymentResult
import ru.modulkassa.payment.library.entity.PaymentResultError
import ru.modulkassa.payment.library.entity.PaymentResultSuccess

class PaymentActivityResultContract : ActivityResultContract<PaymentOptions, PaymentResult>() {

    override fun createIntent(context: Context, input: PaymentOptions) =
        PaymentActivity.createIntent(context, input)

    override fun parseResult(resultCode: Int, intent: Intent?): PaymentResult {
        if (resultCode != Activity.RESULT_OK) {
            return PaymentResultError.fromBundle(intent?.extras)
        }
        return PaymentResultSuccess.fromBundle(intent?.extras)
    }
}