package ru.modulkassa.payment.library.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.modulkassa.payment.library.databinding.ActivityPaymentBinding
import ru.modulkassa.payment.library.entity.PaymentOptions
import ru.modulkassa.payment.library.entity.PaymentResultError

internal class PaymentActivity : AppCompatActivity() {

    companion object {
        private const val KEY_PAYMENT_OPTIONS = "key_payment_options"

        fun createIntent(context: Context, options: PaymentOptions): Intent {
            return Intent(context, PaymentActivity::class.java).apply {
                putExtra(KEY_PAYMENT_OPTIONS, options.toBundle())
            }
        }
    }

    private lateinit var binding: ActivityPaymentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        if (savedInstanceState == null) {
            // do logic
        }
    }

    // todo переписать на OnBackPressedDispatcher
    override fun onBackPressed() {
        val intent = Intent().apply {
            putExtras(PaymentResultError("Отменено пользователем").toBundle())
        }
        setResult(Activity.RESULT_CANCELED, intent)
        super.onBackPressed()
    }
}