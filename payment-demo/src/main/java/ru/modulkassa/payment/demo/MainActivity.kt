package ru.modulkassa.payment.demo

import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import ru.modulkassa.payment.demo.databinding.ActivityMainBinding
import ru.modulkassa.payment.library.ModulPaymentClient
import ru.modulkassa.payment.library.entity.InventPosition
import ru.modulkassa.payment.library.entity.PaymentOptions
import ru.modulkassa.payment.library.entity.PaymentResult
import java.math.BigDecimal

class MainActivity : AppCompatActivity() {

    private val modulPaymentClient = ModulPaymentClient()

    private lateinit var binding: ActivityMainBinding

    private val startPaymentForResult = registerForActivityResult(
        modulPaymentClient.createSbpPaymentContract()
    ) { result: PaymentResult ->
        Toast.makeText(this, "Платеж завершен", Toast.LENGTH_SHORT).show()
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
                )
            )
        )
    }

}