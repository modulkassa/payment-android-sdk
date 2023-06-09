package ru.modulkassa.payment.library.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import ru.modulkassa.payment.library.domain.entity.PaymentOptions
import ru.modulkassa.payment.library.domain.entity.result.PaymentResult
import ru.modulkassa.payment.library.domain.entity.result.PaymentResultError
import ru.modulkassa.payment.library.domain.entity.result.PaymentResultSuccess

class PaymentActivityCreateActionContract : ActivityResultContract<PaymentOptions, PaymentResult>() {

    override fun createIntent(context: Context, input: PaymentOptions) =
        PaymentActivity.createIntent(context, input)

    override fun parseResult(resultCode: Int, intent: Intent?): PaymentResult {
        return if (resultCode == Activity.RESULT_OK) {
            PaymentResultSuccess.fromBundle(intent?.extras)
        } else {
            PaymentResultError.fromBundle(intent?.extras)
        }
    }
}

class PaymentActivityResultActionContract : ActivityResultContract<String, PaymentResult>() {

    override fun createIntent(context: Context, input: String) =
        PaymentActivity.createIntent(context, input)

    override fun parseResult(resultCode: Int, intent: Intent?): PaymentResult {
        return if (resultCode == Activity.RESULT_OK) {
            PaymentResultSuccess.fromBundle(intent?.extras)
        } else {
            PaymentResultError.fromBundle(intent?.extras)
        }
    }
}