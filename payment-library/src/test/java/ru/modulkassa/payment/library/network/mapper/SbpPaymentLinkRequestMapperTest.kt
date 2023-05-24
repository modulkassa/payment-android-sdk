package ru.modulkassa.payment.library.network.mapper

import com.google.common.truth.Truth
import com.google.gson.Gson
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import org.junit.Test
import ru.modulkassa.payment.library.SettingsRepository
import ru.modulkassa.payment.library.domain.entity.PaymentOptions
import ru.modulkassa.payment.library.domain.entity.position.Position
import ru.modulkassa.payment.library.domain.entity.position.PositionType.COMMODITY
import ru.modulkassa.payment.library.ui.ValidationException
import java.math.BigDecimal
import kotlin.test.assertFailsWith

class SbpPaymentLinkRequestMapperTest {

    @Test
    fun ToDto_NoMerchantId_ThrowsValidationException() {
        val options = PaymentOptions.createSbpOptions("orderId", "description", BigDecimal.ONE)
        val repository: SettingsRepository = mock() {
            on { getMerchantId() } doReturn null
        }
        val mapper = getSbpPaymentLinkRequestMapper(repository = repository)
        assertFailsWith(ValidationException::class) {
            mapper.toDto(options)
        }
    }

    @Test
    fun ToDto_WithoutAmont_CalculatesAmount() {
        val options = PaymentOptions.createSbpOptions(
            "orderId", "description", positions = listOf(
                Position(
                    name = "Первый товар",
                    price = BigDecimal.valueOf(12),
                    quantity = BigDecimal.TEN,
                    type = COMMODITY
                )
            )
        )
        val repository: SettingsRepository = mock() {
            on { getMerchantId() } doReturn "merchantId"
        }
        val mapper = getSbpPaymentLinkRequestMapper(repository = repository)

        val request = mapper.toDto(options)
        Truth.assertThat(request.amount).isEqualTo("120")
    }

    @Test
    fun ToDto_DescriptionWithBreaks_FixLineBreaks() {
        val options = PaymentOptions.createSbpOptions("orderId", "description\rdescriprion\n", BigDecimal.TEN)
        val repository: SettingsRepository = mock() {
            on { getMerchantId() } doReturn "merchantId"
        }
        val mapper = getSbpPaymentLinkRequestMapper(repository = repository)

        val request = mapper.toDto(options)
        Truth.assertThat(request.description).isEqualTo("descriptiondescriprion\r\n")
    }

    @Test
    fun ToDto_NoPositions_NullPositions() {
        val options =
            PaymentOptions.createSbpOptions("orderId", "description", BigDecimal.TEN, positions = null)
        val repository: SettingsRepository = mock() {
            on { getMerchantId() } doReturn "merchantId"
        }
        val mapper = getSbpPaymentLinkRequestMapper(repository = repository)

        val request = mapper.toDto(options)
        Truth.assertThat(request.receiptItems).isNull()
    }

    @Test
    fun ToDto_WithPositions_FormatPriceAndQuantity() {
        val options =
            PaymentOptions.createSbpOptions(
                "orderId", "description", positions = listOf(
                    Position(
                        name = "Первый товар",
                        price = BigDecimal.valueOf(12.123),
                        quantity = BigDecimal.valueOf(12.1234),
                        type = COMMODITY
                    )
                )
            )
        val repository: SettingsRepository = mock() {
            on { getMerchantId() } doReturn "merchantId"
        }
        val mapper = getSbpPaymentLinkRequestMapper(repository = repository)

        val request = mapper.toDto(options)
        Truth.assertThat(request.receiptItems).isEqualTo(
            "[{\"name\":\"Первый товар\",\"quantity\":\"12.123\"," +
                "\"price\":\"12.12\",\"sno\":\"osn\",\"payment_object\":\"commodity\",\"payment_method\":\"full_payment\"," +
                "\"vat\":\"vat20\"}]"
        )
    }

    @Test
    fun ToDto_FullFields_Correct() {
        val options =
            PaymentOptions.createSbpOptions(
                orderId = "orderId", description = "description", amount = BigDecimal.valueOf(120),
                positions = listOf(
                    Position(
                        name = "Первый товар",
                        price = BigDecimal.valueOf(12),
                        quantity = BigDecimal.valueOf(10),
                        type = COMMODITY
                    )
                ),
                receiptContact = "receiptContact"
            )
        val repository: SettingsRepository = mock() {
            on { getMerchantId() } doReturn "merchantId"
        }
        val mapper = getSbpPaymentLinkRequestMapper(repository = repository)

        val request = mapper.toDto(options)
        Truth.assertThat(request.merchant).isEqualTo("merchantId")
        Truth.assertThat(request.amount).isEqualTo("120")
        Truth.assertThat(request.orderId).isEqualTo("orderId")
        Truth.assertThat(request.description).isEqualTo("description")
        Truth.assertThat(request.receiptContact).isEqualTo("receiptContact")
        Truth.assertThat(request.receiptItems).isEqualTo(
            "[{\"name\":\"Первый товар\",\"quantity\":\"10\",\"price\":\"12\",\"sno\":\"osn\"," +
                "\"payment_object\":\"commodity\",\"payment_method\":\"full_payment\",\"vat\":\"vat20\"}]"
        )
    }

    private fun getSbpPaymentLinkRequestMapper(
        gson: Gson = Gson(),
        repository: SettingsRepository = mock(),
    ): SbpPaymentLinkRequestMapper {
        return SbpPaymentLinkRequestMapper(gson, repository)
    }
}