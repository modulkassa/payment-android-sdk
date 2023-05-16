package ru.modulkassa.payment.library.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.modulkassa.payment.library.R
import ru.modulkassa.payment.library.domain.entity.PaymentOptions
import ru.modulkassa.payment.library.domain.entity.result.ErrorType
import ru.modulkassa.payment.library.domain.entity.result.PaymentResultError

internal class PaymentActivity : AppCompatActivity() {

    companion object {
        const val KEY_ACTION = "action"
        const val CREATE_PAYMENT_ACTION = "create_payment"
        const val GET_PAYMENT_RESULT_ACTION = "get_payment_result"
        const val KEY_ORDER_ID = "order_id"


        fun createIntent(context: Context, options: PaymentOptions): Intent {
            return Intent(context, PaymentActivity::class.java).apply {
                putExtra(KEY_ACTION, CREATE_PAYMENT_ACTION)
                putExtras(options.toBundle())
            }
        }

        fun createIntent(context: Context, orderId: String): Intent {
            return Intent(context, PaymentActivity::class.java).apply {
                putExtra(KEY_ACTION, GET_PAYMENT_RESULT_ACTION)
                putExtra(KEY_ORDER_ID, orderId)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            PaymentBottomSheetFragment.create()
                .show(supportFragmentManager, PaymentBottomSheetFragment.TAG)
        }
    }
}