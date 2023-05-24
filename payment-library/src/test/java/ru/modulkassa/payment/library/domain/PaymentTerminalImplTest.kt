package ru.modulkassa.payment.library.domain

import com.google.common.truth.Truth
import com.google.gson.Gson
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import io.reactivex.rxjava3.core.Single
import okhttp3.ResponseBody
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import ru.modulkassa.payment.library.SettingsRepository
import ru.modulkassa.payment.library.domain.entity.PaymentOptions
import ru.modulkassa.payment.library.domain.entity.result.PaymentResultSuccess
import ru.modulkassa.payment.library.network.PaymentApi
import ru.modulkassa.payment.library.network.dto.SbpPaymentLinkRequestDto
import ru.modulkassa.payment.library.network.dto.SbpPaymentLinkResponseDto
import ru.modulkassa.payment.library.network.dto.TransactionDto
import ru.modulkassa.payment.library.network.dto.TransactionResponseDto
import ru.modulkassa.payment.library.network.dto.TransactionStateDto.COMPLETE
import ru.modulkassa.payment.library.network.dto.TransactionStateDto.FAILED
import ru.modulkassa.payment.library.ui.NetworkException
import ru.modulkassa.payment.library.ui.PaymentFailedException
import ru.modulkassa.payment.library.ui.ValidationException
import java.math.BigDecimal

class PaymentTerminalImplTest {

    @Test
    fun CreateSbpPaymentLink_WrongOptions_ThrowsValidationException() {
        val options = PaymentOptions.createSbpOptions("orderId", "description", BigDecimal.ONE)
        val repository: SettingsRepository = mock {
            on { getMerchantId() } doReturn null
        }
        val terminal = createTerminal(repository = repository)

        terminal.createSbpPaymentLink(options)
            .test()
            .assertFailure(ValidationException::class.java)
    }

    @Test
    fun CreateSbpPaymentLink_CorrectOptions_RequestSigned() {
        val options = PaymentOptions.createSbpOptions("orderId", "description", BigDecimal.ONE)
        val api: PaymentApi = mock {
            on { createSbpPayment(any()) } doReturn Single.just(SbpPaymentLinkResponseDto("sbpLink"))
        }
        val terminal = createTerminal(api = api)

        terminal.createSbpPaymentLink(options)
            .test()
            .assertComplete()

        argumentCaptor<SbpPaymentLinkRequestDto>().apply {
            verify(api).createSbpPayment(capture())
            Truth.assertThat(firstValue.signature).isNotEmpty()
        }
    }

    @Test
    fun CreateSbpPaymentLink_Api200Error_ThrowsNetworkException() {
        val options = PaymentOptions.createSbpOptions("orderId", "description", BigDecimal.ONE)
        val response = "{\"sbpLink\":\"sbpLink\",\"status\":\"ERROR\",\"message\":\"message\"}"
        val sbpPaymentLinkResponseDto = Gson().fromJson(response, SbpPaymentLinkResponseDto::class.java)
        val api: PaymentApi = mock {
            on { createSbpPayment(any()) } doReturn Single.just(sbpPaymentLinkResponseDto)
        }
        val terminal = createTerminal(api = api)

        terminal.createSbpPaymentLink(options)
            .test()
            .assertError { error ->
                Truth.assertThat(error).isInstanceOf(NetworkException::class.java)
                Truth.assertThat(error.message).isEqualTo("message")
                true
            }
    }

    @Test
    fun CreateSbpPaymentLink_ApiError_ThrowsNetworkException() {
        val options = PaymentOptions.createSbpOptions("orderId", "description", BigDecimal.ONE)
        val content = "{\"message\":\"message\"}"
        val httpException = HttpException(Response.error<String>(400, ResponseBody.create(null, content)))
        val api: PaymentApi = mock {
            on { createSbpPayment(any()) } doReturn Single.error(httpException)
        }
        val terminal = createTerminal(api = api)

        terminal.createSbpPaymentLink(options)
            .test()
            .assertError { error ->
                Truth.assertThat(error).isInstanceOf(NetworkException::class.java)
                Truth.assertThat(error.message).isEqualTo("message")
                true
            }
    }

    @Test
    fun CreateSbpPaymentLink_UnknownApiError_ThrowsNetworkException() {
        val options = PaymentOptions.createSbpOptions("orderId", "description", BigDecimal.ONE)
        val api: PaymentApi = mock {
            on { createSbpPayment(any()) } doReturn Single.error(Throwable())
        }
        val terminal = createTerminal(api = api)

        terminal.createSbpPaymentLink(options)
            .test()
            .assertError { error ->
                Truth.assertThat(error).isInstanceOf(Throwable::class.java)
                true
            }
    }

    @Test
    fun CreateSbpPaymentLink_ApiCorrect_ReturnsSbpLink() {
        val options = PaymentOptions.createSbpOptions("orderId", "description", BigDecimal.ONE)
        val api: PaymentApi = mock {
            on { createSbpPayment(any()) } doReturn Single.just(SbpPaymentLinkResponseDto("sbpLink"))
        }
        val terminal = createTerminal(api = api)

        terminal.createSbpPaymentLink(options)
            .test()
            .assertValue("sbpLink")
    }

    @Test
    fun GetPaymentStatus_NoMerchantId_ThrowsValidationException() {
        val repository: SettingsRepository = mock {
            on { getMerchantId() } doReturn null
        }
        val terminal = createTerminal(repository = repository)

        terminal.getPaymentStatus("orderId")
            .test()
            .assertFailure(ValidationException::class.java)
    }

    @Test
    fun GetPaymentStatus_ByDefault_RequestSigned() {
        val api: PaymentApi = mock {
            on { getTransaction(any(), any(), any(), any(), any()) } doReturn Single.just(
                TransactionResponseDto(
                    TransactionDto(state = COMPLETE)
                )
            )
        }
        val terminal = createTerminal(api = api)

        terminal.getPaymentStatus("orderId")
            .test()
            .assertComplete()

        argumentCaptor<String>().apply {
            verify(api).getTransaction(any(), any(), capture(), any(), any())
            Truth.assertThat(firstValue).isNotEmpty()
        }
    }

    @Test
    fun GetPaymentStatus_Api200Error_ThrowsNetworkException() {
        val response = "{\"state\":\"COMPLETE\",\"status\":\"ERROR\",\"message\":\"message\"}"
        val transactionResponseDto = Gson().fromJson(response, TransactionResponseDto::class.java)
        val api: PaymentApi = mock {
            on { getTransaction(any(), any(), any(), any(), any()) } doReturn Single.just(transactionResponseDto)
        }
        val terminal = createTerminal(api = api)

        terminal.getPaymentStatus("orderId")
            .test()
            .assertError { error ->
                Truth.assertThat(error).isInstanceOf(NetworkException::class.java)
                Truth.assertThat(error.message).isEqualTo("message")
                true
            }
    }

    @Test
    fun GetPaymentStatus_UnknownApiError_ThrowsNetworkException() {
        val api: PaymentApi = mock {
            on { getTransaction(any(), any(), any(), any(), any()) } doReturn Single.error(Throwable())
        }
        val terminal = createTerminal(api = api)

        terminal.getPaymentStatus("orderId")
            .test()
            .assertError { error ->
                Truth.assertThat(error).isInstanceOf(Throwable::class.java)
                true
            }
    }

    @Test
    fun GetPaymentStatus_StateComplete_PaymentResultSuccess() {
        val api: PaymentApi = mock {
            on { getTransaction(any(), any(), any(), any(), any()) } doReturn Single.just(
                TransactionResponseDto(
                    TransactionDto(
                        state = COMPLETE,
                        transactionId = "transactionId",
                        sbpTransactionId = "sbpTransactionId"
                    )
                )
            )
        }
        val terminal = createTerminal(api = api)

        terminal.getPaymentStatus("orderId")
            .test()
            .assertValue { value ->
                Truth.assertThat(value).isInstanceOf(PaymentResultSuccess::class.java)
                Truth.assertThat(value.transactionId).isEqualTo("transactionId")
                Truth.assertThat(value.sbpTransactionId).isEqualTo("sbpTransactionId")
                true
            }
    }

    @Test
    fun GetPaymentStatus_StateFailed_PaymentFailedException() {
        val api: PaymentApi = mock {
            on { getTransaction(any(), any(), any(), any(), any()) } doReturn Single.just(
                TransactionResponseDto(
                    TransactionDto(
                        state = FAILED,
                        message = "message"
                    )
                )
            )
        }
        val terminal = createTerminal(api = api)

        terminal.getPaymentStatus("orderId")
            .test()
            .assertError { error ->
                Truth.assertThat(error).isInstanceOf(PaymentFailedException::class.java)
                Truth.assertThat(error.message).isEqualTo("message")
                true
            }
    }

    private fun createTerminal(
        api: PaymentApi = mock(),
        gson: Gson = Gson(),
        repository: SettingsRepository = mock {
            on { getMerchantId() } doReturn "merchantId"
            on { getSignatureKey() } doReturn "signatureKey"
        }
    ): PaymentTerminal {
        return PaymentTerminalImpl(api, gson, repository)
    }

}