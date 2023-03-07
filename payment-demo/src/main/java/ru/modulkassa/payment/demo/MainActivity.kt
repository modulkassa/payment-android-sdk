package ru.modulkassa.payment.demo

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ru.modulkassa.payment.demo.databinding.ActivityMainBinding
import ru.modulkassa.payment.library.ModulPaymentClient
import ru.modulkassa.payment.library.entity.InventPosition
import ru.modulkassa.payment.library.entity.PaymentOptions
import ru.modulkassa.payment.library.entity.PaymentResult
import ru.modulkassa.payment.library.entity.PaymentResultError
import ru.modulkassa.payment.library.entity.PaymentResultSuccess
import java.math.BigDecimal

class MainActivity : AppCompatActivity() {

    private val modulPaymentClient = ModulPaymentClient()

    private lateinit var binding: ActivityMainBinding

    private val startPaymentForResult = registerForActivityResult(
        modulPaymentClient.createSbpPaymentContract()
    ) { result: PaymentResult ->
        when (result) {
            is PaymentResultSuccess -> {
                Toast.makeText(this, "Платеж завершен", Toast.LENGTH_SHORT).show()
            }
            is PaymentResultError -> {
                Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.payBySbp.setOnClickListener {
            startPaymentForResult.launch(createDemoPaymentOptions())
        }
    }

    private fun createDemoPaymentOptions(): PaymentOptions {
        return PaymentOptions(
            description = "Такой вот длинный, но не очень тестовый платеж",
            listOf(
                InventPosition(
                    name = "Первая позиция",
                    price = BigDecimal.TEN,
                    quantity = BigDecimal.ONE
                ),
                InventPosition(
                    name = "Вторая позиция",
                    price = BigDecimal("20"),
                    quantity = BigDecimal.ONE
                ),
                InventPosition(
                    name = "Третья позиция",
                    price = BigDecimal("30"),
                    quantity = BigDecimal.ONE
                ),
                InventPosition(
                    name = "Четвертая позиция",
                    price = BigDecimal("40"),
                    quantity = BigDecimal.ONE
                ),
                InventPosition(
                    name = "Пятая позиция",
                    price = BigDecimal("50"),
                    quantity = BigDecimal.TEN
                )
            )
        )
    }

}