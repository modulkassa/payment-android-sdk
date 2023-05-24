package ru.modulkassa.payment.demo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.modulkassa.payment.demo.databinding.ActivityMainBinding
import ru.modulkassa.payment.library.ModulPaymentClient
import ru.modulkassa.payment.library.domain.entity.PaymentOptions
import ru.modulkassa.payment.library.domain.entity.position.Position
import ru.modulkassa.payment.library.domain.entity.position.PositionType
import ru.modulkassa.payment.library.domain.entity.result.PaymentResult
import ru.modulkassa.payment.library.domain.entity.result.PaymentResultError
import ru.modulkassa.payment.library.domain.entity.result.PaymentResultSuccess
import java.math.BigDecimal
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private val modulPaymentClient = ModulPaymentClient().apply {
        registerPaymentResultCallback(this@MainActivity) { result: PaymentResult ->
            when (result) {
                is PaymentResultSuccess -> {
                    binding.resultDescription.text = "Оплата прошла успешно:\n" +
                        "transactionId=${result.transactionId},\n" +
                        "sbpTransactionID=${result.sbpTransactionId}"
                }
                is PaymentResultError -> {
                    val message = result.message
                    binding.resultDescription.text = if (result.cause.isNotBlank()) {
                        "$message : ${result.type.name} : ${result.cause}"
                    } else {
                        "$message : ${result.type.name}"
                    }
                }
            }
        }
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        modulPaymentClient.init(
            this,
            merchantId = BuildConfig.MERCHANT_ID,
            signatureKey = BuildConfig.SIGNATURE_KEY
        )

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.payBySbp.setOnClickListener {
            binding.resultDescription.text = ""
            val orderId = binding.orderId.text.toString().ifBlank {
                val randomOrderId = Random.nextInt(1, 1000).toString()
                binding.orderId.setText(randomOrderId)
                randomOrderId
            }
            modulPaymentClient.payBySbp(
                options = createSingleSbpDemoOptions(orderId)
            )
        }

        binding.checkPaymentResult.setOnClickListener {
            binding.resultDescription.text = ""
            val orderId = binding.orderId.text.toString().ifBlank {
                binding.orderId.error = getString(R.string.error_no_order_id)
                null
            }
            if (orderId != null) {
                modulPaymentClient.getPaymentResult(orderId)
            }
        }
    }

    private fun createSingleSbpDemoOptions(
        orderId: String
    ): PaymentOptions {
        return PaymentOptions.createSbpOptions(
            orderId = orderId,
            description = "Заказ №$orderId",
            positions = listOf(
                Position(
                    name = "Первый товар",
                    price = BigDecimal.valueOf(0.50),
                    quantity = BigDecimal.ONE,
                    type = PositionType.COMMODITY
                ),
                Position(
                    name = "Вторая услуга",
                    price = BigDecimal("0.50"),
                    quantity = BigDecimal.ONE,
                    type = PositionType.SERVICE
                )
            )
        )
    }

}