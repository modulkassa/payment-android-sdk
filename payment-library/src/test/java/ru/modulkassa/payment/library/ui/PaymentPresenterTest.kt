package ru.modulkassa.payment.library.ui

import android.content.Context
import com.google.common.truth.Truth
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import io.reactivex.rxjava3.core.Single
import org.junit.Rule
import org.junit.Test
import ru.modulkassa.payment.library.R
import ru.modulkassa.payment.library.RxRule
import ru.modulkassa.payment.library.domain.PaymentTerminal
import ru.modulkassa.payment.library.domain.entity.PaymentOptions
import ru.modulkassa.payment.library.domain.entity.position.Position
import ru.modulkassa.payment.library.domain.entity.position.PositionType.PAYMENT
import ru.modulkassa.payment.library.domain.entity.result.PaymentResultSuccess
import java.math.BigDecimal
import java.util.concurrent.TimeoutException

class PaymentPresenterTest {

    @Rule
    @JvmField
    val rule = RxRule()

    @Test
    fun CheckPaymentOptionsAndShow_ByDefault_ShowsDescriptionAndPositions() {
        val presenter = createPresenter()
        val view = mock<PaymentView>()
        presenter.attachView(view)

        val positions: List<Position> = listOf(
            Position("name", BigDecimal.valueOf(10.55), BigDecimal.ONE, PAYMENT)
        )
        val options = getOptions(positions = positions)
        presenter.checkPaymentOptionsAndShow(options)

        verify(view, times(1)).showDescription("description")
        verify(view, times(1)).showPositions(positions)
        verify(view, times(1)).showSum(BigDecimal.valueOf(10.55))
    }

    @Test
    fun CheckPaymentOptionsAndShow_EmptyPositions_NeverShowPositions() {
        val presenter = createPresenter()
        val view = mock<PaymentView>()
        presenter.attachView(view)

        val options = getOptions(amount = BigDecimal.TEN, positions = emptyList())
        presenter.checkPaymentOptionsAndShow(options)

        verify(view, never()).showPositions(any())
        verify(view, times(1)).showSum(BigDecimal.TEN)
    }

    @Test
    fun CheckPaymentOptionsAndShow_AmountAndPositionNotDefined_SetValidationErrorResult() {
        val presenter = createPresenter()
        val view = mock<PaymentView>()
        presenter.attachView(view)

        val options = getOptions(amount = null, positions = emptyList())
        presenter.checkPaymentOptionsAndShow(options)

        verify(view).setErrorResult(any<ValidationErrorResult>())
    }

    @Test
    fun PayBySbp_ByDefault_SendSbpLink() {
        val terminal: PaymentTerminal = mock {
            on { createSbpPaymentLink(any()) } doReturn Single.just("sbpLink")
        }
        val presenter = createPresenter(terminal = terminal)
        val view = mock<PaymentView>()
        presenter.attachView(view)

        presenter.payBySbp(getOptions())

        verify(view).showProgress(R.string.create_payment_progress)
        verify(view).hideProgress()
        verify(view).sendSbpLink("sbpLink")
    }

    @Test
    fun PayBySbp_UnexpectedError_SetErrorResult() {
        val terminal: PaymentTerminal = mock {
            on { createSbpPaymentLink(any()) } doReturn Single.error(Throwable())
        }
        val presenter = createPresenter(terminal = terminal)
        val view = mock<PaymentView>()
        presenter.attachView(view)

        presenter.payBySbp(getOptions())

        verify(view).showProgress(R.string.create_payment_progress)
        verify(view).setErrorResult(any<BaseErrorResult>())
    }

    @Test
    fun PayBySbp_ValidationException_SetValidationErrorResult() {
        val terminal: PaymentTerminal = mock {
            on { createSbpPaymentLink(any()) } doReturn Single.error(ValidationException("especial cause", 123))
        }
        val presenter = createPresenter(terminal = terminal)
        val view = mock<PaymentView>()
        presenter.attachView(view)

        presenter.payBySbp(getOptions())

        argumentCaptor<BaseErrorResult>().apply {
            verify(view).setErrorResult(capture())
            Truth.assertThat(firstValue).isInstanceOf(ValidationErrorResult::class.java)
            val cause = firstValue.toPaymentResultError(getContextMock()).cause
            Truth.assertThat(cause).isEqualTo("especial cause")
        }
    }

    @Test
    fun PayBySbp_TimeoutException_SetTimeoutErrorResult() {
        val terminal: PaymentTerminal = mock {
            on { createSbpPaymentLink(any()) } doReturn Single.error(TimeoutException())
        }
        val presenter = createPresenter(terminal = terminal)
        val view = mock<PaymentView>()
        presenter.attachView(view)

        presenter.payBySbp(getOptions())

        argumentCaptor<BaseErrorResult>().apply {
            verify(view).setErrorResult(capture())
            Truth.assertThat(firstValue).isInstanceOf(TimeoutErrorResult::class.java)
        }
    }

    @Test
    fun PayBySbp_NetworkException_SetNetworkErrorResult() {
        val terminal: PaymentTerminal = mock {
            on { createSbpPaymentLink(any()) } doReturn Single.error(NetworkException("especial cause"))
        }
        val presenter = createPresenter(terminal = terminal)
        val view = mock<PaymentView>()
        presenter.attachView(view)

        presenter.payBySbp(getOptions())

        argumentCaptor<BaseErrorResult>().apply {
            verify(view).setErrorResult(capture())
            Truth.assertThat(firstValue).isInstanceOf(NetworkErrorResult::class.java)
            val cause = firstValue.toPaymentResultError(getContextMock()).cause
            Truth.assertThat(cause).isEqualTo("especial cause")
        }
    }

    @Test
    fun PayBySbp_PaymentFailedException_SetPaymentFailedErrorResult() {
        val terminal: PaymentTerminal = mock {
            on { createSbpPaymentLink(any()) } doReturn Single.error(PaymentFailedException("especial cause"))
        }
        val presenter = createPresenter(terminal = terminal)
        val view = mock<PaymentView>()
        presenter.attachView(view)

        presenter.payBySbp(getOptions())

        argumentCaptor<BaseErrorResult>().apply {
            verify(view).setErrorResult(capture())
            Truth.assertThat(firstValue).isInstanceOf(PaymentFailedErrorResult::class.java)
            val cause = firstValue.toPaymentResultError(getContextMock()).cause
            Truth.assertThat(cause).isEqualTo("especial cause")
        }
    }

    @Test
    fun PayBySbp_SomeException_SetBaseErrorResult() {
        val terminal: PaymentTerminal = mock {
            on { createSbpPaymentLink(any()) } doReturn Single.error(Throwable("especial cause"))
        }
        val presenter = createPresenter(terminal = terminal)
        val view = mock<PaymentView>()
        presenter.attachView(view)

        presenter.payBySbp(getOptions())

        argumentCaptor<BaseErrorResult>().apply {
            verify(view).setErrorResult(capture())
            Truth.assertThat(firstValue).isInstanceOf(BaseErrorResult::class.java)
            val cause = firstValue.toPaymentResultError(getContextMock()).cause
            Truth.assertThat(cause).isEqualTo("especial cause")
        }
    }

    @Test
    fun GetPaymentResult_ByDefault_SetSuccessResult() {
        val result = PaymentResultSuccess("transactionId", "sbpTransactionId")
        val terminal: PaymentTerminal = mock {
            on { getPaymentStatus(any()) } doReturn Single.just(result)
        }
        val presenter = createPresenter(terminal = terminal)
        val view = mock<PaymentView>()
        presenter.attachView(view)

        presenter.getPaymentResult("orderId")

        verify(view).showProgress(R.string.check_payment_progress)
        verify(view).setSuccessResult(result)
    }

    @Test
    fun GetPaymentResult_UnexpectedError_SetErrorResult() {
        val terminal: PaymentTerminal = mock {
            on { getPaymentStatus(any()) } doReturn Single.error(Throwable())
        }
        val presenter = createPresenter(terminal = terminal)
        val view = mock<PaymentView>()
        presenter.attachView(view)

        presenter.getPaymentResult("orderId")

        verify(view).showProgress(R.string.check_payment_progress)
        verify(view).setErrorResult(any<BaseErrorResult>())
    }

    private fun createPresenter(
        terminal: PaymentTerminal = mock()
    ): PaymentPresenter {
        return PaymentPresenter(terminal)
    }

    private fun getOptions(
        orderId: String = "order-id",
        description: String = "description",
        positions: List<Position> = listOf(
            Position("name", BigDecimal.valueOf(10.55), BigDecimal.ONE, PAYMENT)
        ),
        amount: BigDecimal? = BigDecimal.valueOf(10.55),
    ): PaymentOptions {
        return PaymentOptions.createSbpOptions(
            orderId = orderId,
            description = description,
            positions = positions,
            amount = amount
        )
    }

    private fun getContextMock() = mock<Context> {
        on { getString(any()) } doReturn "some string"
    }

}