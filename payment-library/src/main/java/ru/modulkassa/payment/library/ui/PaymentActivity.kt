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
        fun createIntent(context: Context, options: PaymentOptions): Intent {
            return Intent(context, PaymentActivity::class.java).apply {
                putExtras(options.toBundle())
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

    // todo переписать на OnBackPressedDispatcher
    override fun onBackPressed() {
        val intent = Intent().apply {
            putExtras(PaymentResultError(getString(R.string.error_result_cancelled_by_user), ErrorType.CANCELLED).toBundle())
        }
        setResult(Activity.RESULT_CANCELED, intent)
        super.onBackPressed()
    }
}