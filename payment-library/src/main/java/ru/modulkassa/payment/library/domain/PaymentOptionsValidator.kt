package ru.modulkassa.payment.library.domain

import ru.modulkassa.payment.library.domain.entity.PaymentOptions
import ru.modulkassa.payment.library.ui.ValidationException
import java.math.BigDecimal

/**
 * Проверяет параметры платежа на валидность
 * @throws [ValidationException] если какое-то поле не прошло валидацию
 */
internal object PaymentOptionsValidator {

    @Throws(ValidationException::class)
    fun validate(options: PaymentOptions) {
        println("Валидируем поля запроса на оплату $options")

        if (options.positions.isNullOrEmpty() && options.amount == null) {
            throw ValidationException("Необходимо указать либо позиции для оплаты, либо сумму для оплаты")
        }

        if (options.positions?.isEmpty() == false && options.amount != null
            && options.amount.compareTo(options.calculateAmount()) != 0
        ) {
            throw ValidationException("Сумма по всем позициям должна совпадать со значением в поле amount")
        }

        FieldValidator(options.orderId, "orderId")
            .apply {
                checkNotBlank()
                checkLength(50)
                checkOnlyPrintableAscii()
            }

        FieldValidator(options.description, "description")
            .apply {
                checkNotBlank()
                checkLength(250)
            }

        options.receiptContact?.let {
            FieldValidator(it, "receiptContact")
                .apply {
                    checkNotBlank()
                    checkLength(64)
                }
        }

        options.positions?.toList()?.forEach { position ->
            FieldValidator(position.name, "position.name")
                .apply {
                    checkNotBlank()
                    checkLength(128)
                }

            if (position.quantity.compareTo(BigDecimal("99999.999")) > 1) {
                throw ValidationException("Слишком большое значение в поле position.quantity - ${position.quantity}")
            }

            if (position.price.compareTo(BigDecimal("99999999.99")) > 1) {
                throw ValidationException("Слишком большое значение в поле position.price - ${position.price}")
            }
        }
    }
}

internal class FieldValidator(
    private val fieldValue: String,
    private val fieldName: String
) {
    fun checkNotBlank() {
        if (fieldValue.isBlank()) {
            throw ValidationException("Пустое значение в поле $fieldName - $fieldValue")
        }
    }

    fun checkLength(fieldLength: Int) {
        if (fieldValue.length > fieldLength) {
            throw ValidationException(
                "Слишком длинное значение в поле $fieldName - $fieldValue (максимально $fieldLength символов)"
            )
        }
    }

    fun checkOnlyPrintableAscii() {
        if (containsNotOnlyPrintableAsciiSymbols(fieldValue)) {
            throw ValidationException("Присутствуют непечатные Ascii символы в поле $fieldName - $fieldValue")
        }
    }

    private fun containsNotOnlyPrintableAsciiSymbols(text: String): Boolean {
        return text.toCharArray().any { it < 32.toChar() || it > 127.toChar() }
    }

}