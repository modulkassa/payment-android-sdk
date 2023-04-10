package ru.modulkassa.payment.demo

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ru.modulkassa.payment.demo.databinding.ActivityMainBinding
import ru.modulkassa.payment.library.ModulPaymentClient
import ru.modulkassa.payment.library.domain.entity.PaymentOptions
import ru.modulkassa.payment.library.domain.entity.position.PaymentMethod
import ru.modulkassa.payment.library.domain.entity.position.PaymentObject
import ru.modulkassa.payment.library.domain.entity.position.Position
import ru.modulkassa.payment.library.domain.entity.position.TaxationMode
import ru.modulkassa.payment.library.domain.entity.position.VatTag
import ru.modulkassa.payment.library.domain.entity.result.PaymentResult
import ru.modulkassa.payment.library.domain.entity.result.PaymentResultError
import ru.modulkassa.payment.library.domain.entity.result.PaymentResultSuccess
import java.math.BigDecimal

class MainActivity : AppCompatActivity() {

    private val modulPaymentClient = ModulPaymentClient(
        merchantId = BuildConfig.MERCHANT_ID,
        signatureKey = BuildConfig.SIGNATURE_KEY
    ).apply {
        registerActivityCallback(this@MainActivity) { result: PaymentResult ->
            when (result) {
                is PaymentResultSuccess -> {
                    Toast.makeText(this@MainActivity, "Платеж завершен", Toast.LENGTH_SHORT).show()
                }
                is PaymentResultError -> {
                    Toast.makeText(
                        this@MainActivity,
                        result.message + ": " + result.cause,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.payBySbp.setOnClickListener {
            modulPaymentClient.payBySbp(
                options = createSingleSbpDemoOptions()
            )
        }
    }

    private fun createSingleSbpDemoOptions(): PaymentOptions {
        return PaymentOptions.createSbpOptions(
            orderId = "14425840",
            description = "Заказ №14425840",
            positions = listOf(
                Position(
                    name = "Первая позиция",
                    price = BigDecimal.valueOf(1200.10),
                    quantity = BigDecimal.ONE,
                    taxationMode = TaxationMode.OSN,
                    paymentObject = PaymentObject.COMMODITY,
                    paymentMethod = PaymentMethod.FULL_PAYMENT,
                    vat = VatTag.VAT_20
                ),
                Position(
                    name = "Вторая позиция",
                    price = BigDecimal("20"),
                    quantity = BigDecimal.TEN,
                    taxationMode = TaxationMode.OSN,
                    paymentObject = PaymentObject.COMMODITY,
                    paymentMethod = PaymentMethod.FULL_PAYMENT,
                    vat = VatTag.VAT_20
                )
            )
        )
    }

}