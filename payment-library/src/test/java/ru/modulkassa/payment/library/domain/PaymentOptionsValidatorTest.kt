package ru.modulkassa.payment.library.domain

import org.junit.Test
import ru.modulkassa.payment.library.domain.entity.PaymentOptions
import ru.modulkassa.payment.library.domain.entity.position.Position
import ru.modulkassa.payment.library.domain.entity.position.PositionType
import ru.modulkassa.payment.library.domain.entity.position.TaxationMode
import ru.modulkassa.payment.library.ui.ValidationException
import java.math.BigDecimal
import kotlin.test.assertFailsWith

class PaymentOptionsValidatorTest {

    @Test
    fun Validate_Correct_NoException() {
        PaymentOptionsValidator.validate(createDefaultOptions())
    }

    @Test
    fun Validate_NoPositionsNoAmount_ExceptionCought() {
        assertFailsWith(ValidationException::class) {
            PaymentOptionsValidator.validate(createDefaultOptions(amount = null, positions = null))
        }
    }

    @Test
    fun Validate_EmptyPositionsNoAmount_ExceptionCought() {
        assertFailsWith(ValidationException::class) {
            PaymentOptionsValidator.validate(createDefaultOptions(amount = null, positions = emptyList()))
        }
    }

    @Test
    fun Validate_PositionSumAmountAreNotEqual_ExceptionCought() {
        assertFailsWith(ValidationException::class) {
            PaymentOptionsValidator.validate(
                createDefaultOptions(
                    amount = BigDecimal.TEN,
                    positions = listOf(
                        Position(
                            name = "Первая позиция",
                            price = BigDecimal.ONE,
                            quantity = BigDecimal.ONE,
                            taxationMode = TaxationMode.OSN,
                            type = PositionType.COMMODITY
                        )
                    ),
                )
            )
        }
    }

    @Test
    fun Validate_TooLongOrderId_ExceptionCought() {
        assertFailsWith(ValidationException::class) {
            PaymentOptionsValidator.validate(createDefaultOptions(orderId = "1".repeat(51)))
        }
    }

    @Test
    fun Validate_OrderIdIsBlank_ExceptionCought() {
        assertFailsWith(ValidationException::class) {
            PaymentOptionsValidator.validate(createDefaultOptions(orderId = "   "))
        }
    }

    @Test
    fun Validate_OrderIdNotAscii_ExceptionCought() {
        assertFailsWith(ValidationException::class) {
            PaymentOptionsValidator.validate(createDefaultOptions(orderId = "я"))
        }
    }

    @Test
    fun Validate_TooLongDescription_ExceptionCought() {
        assertFailsWith(ValidationException::class) {
            PaymentOptionsValidator.validate(createDefaultOptions(description = "1".repeat(251)))
        }
    }

    @Test
    fun Validate_DescriptionIsBlank_ExceptionCought() {
        assertFailsWith(ValidationException::class) {
            PaymentOptionsValidator.validate(createDefaultOptions(description = "   "))
        }
    }

    @Test
    fun Validate_ReceiptContactIsNull_NoException() {
        PaymentOptionsValidator.validate(createDefaultOptions(receiptContact = null))
    }

    @Test
    fun Validate_TooLongReceiptContact_ExceptionCought() {
        assertFailsWith(ValidationException::class) {
            PaymentOptionsValidator.validate(createDefaultOptions(receiptContact = "1".repeat(65)))
        }
    }

    @Test
    fun Validate_ReceiptContactIsBlank_ExceptionCought() {
        assertFailsWith(ValidationException::class) {
            PaymentOptionsValidator.validate(createDefaultOptions(receiptContact = "   "))
        }
    }

    @Test
    fun Validate_TooLongPositionName_ExceptionCought() {
        assertFailsWith(ValidationException::class) {
            PaymentOptionsValidator.validate(
                createDefaultOptions(
                    amount = null,
                    positions = listOf(
                        Position(
                            name = "1".repeat(129),
                            price = BigDecimal.TEN,
                            quantity = BigDecimal.ONE,
                            type = PositionType.COMMODITY
                        )
                    )
                )
            )
        }
    }

    @Test
    fun Validate_PositionNameIsBlank_NoException() {
        assertFailsWith(ValidationException::class) {
            PaymentOptionsValidator.validate(
                createDefaultOptions(
                    amount = null,
                    positions = listOf(
                        Position(
                            name = "  ",
                            price = BigDecimal.TEN,
                            quantity = BigDecimal.ONE,
                            type = PositionType.COMMODITY
                        )
                    )
                )
            )
        }
    }

    @Test
    fun Validate_TooMuchQantity_ExceptionCought() {
        assertFailsWith(ValidationException::class) {
            PaymentOptionsValidator.validate(
                createDefaultOptions(
                    amount = null,
                    positions = listOf(
                        Position(
                            name = "Первая позиция",
                            price = BigDecimal.TEN,
                            quantity = BigDecimal.valueOf(100000.000),
                            type = PositionType.COMMODITY
                        )
                    )
                )
            )
        }
    }

    @Test
    fun Validate_NegativeQantity_ExceptionCought() {
        assertFailsWith(ValidationException::class) {
            PaymentOptionsValidator.validate(
                createDefaultOptions(
                    amount = null,
                    positions = listOf(
                        Position(
                            name = "Первая позиция",
                            price = BigDecimal.TEN,
                            quantity = BigDecimal.valueOf(-10),
                            type = PositionType.COMMODITY
                        )
                    )
                )
            )
        }
    }

    @Test
    fun Validate_ZeroQantity_ExceptionCought() {
        assertFailsWith(ValidationException::class) {
            PaymentOptionsValidator.validate(
                createDefaultOptions(
                    amount = null,
                    positions = listOf(
                        Position(
                            name = "Первая позиция",
                            price = BigDecimal.TEN,
                            quantity = BigDecimal.valueOf(00.000),
                            type = PositionType.COMMODITY
                        )
                    )
                )
            )
        }
    }

    @Test
    fun Validate_TooMuchPrice_ExceptionCought() {
        assertFailsWith(ValidationException::class) {
            PaymentOptionsValidator.validate(
                createDefaultOptions(
                    amount = null,
                    positions = listOf(
                        Position(
                            name = "Первая позиция",
                            price = BigDecimal.valueOf(1000000000.00),
                            quantity = BigDecimal.ONE,
                            type = PositionType.COMMODITY
                        )
                    )
                )
            )
        }
    }

    @Test
    fun Validate_NegativePrice_ExceptionCought() {
        assertFailsWith(ValidationException::class) {
            PaymentOptionsValidator.validate(
                createDefaultOptions(
                    amount = null,
                    positions = listOf(
                        Position(
                            name = "Первая позиция",
                            price = BigDecimal.valueOf(-10.00),
                            quantity = BigDecimal.ONE,
                            type = PositionType.COMMODITY
                        )
                    )
                )
            )
        }
    }


    @Test
    fun Validate_ZeroPrice_ExceptionCought() {
        assertFailsWith(ValidationException::class) {
            PaymentOptionsValidator.validate(
                createDefaultOptions(
                    amount = null,
                    positions = listOf(
                        Position(
                            name = "Первая позиция",
                            price = BigDecimal.valueOf(00.00),
                            quantity = BigDecimal.ONE,
                            type = PositionType.COMMODITY
                        )
                    )
                )
            )
        }
    }

    private fun createDefaultOptions(
        orderId: String = "14425840",
        description: String = "Заказ №14425840",
        amount: BigDecimal? = BigDecimal.TEN,
        positions: List<Position>? = listOf(
            Position(
                name = "Первая позиция",
                price = BigDecimal.TEN,
                quantity = BigDecimal.ONE,
                type = PositionType.COMMODITY
            )
        ),
        receiptContact: String? = null
    ) = PaymentOptions.createSbpOptions(
        orderId = orderId,
        description = description,
        amount = amount,
        positions = positions,
        receiptContact = receiptContact
    )
}