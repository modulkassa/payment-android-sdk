package ru.modulkassa.payment.library.ui

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import org.junit.Test
import ru.modulkassa.payment.library.domain.entity.PaymentOptions
import ru.modulkassa.payment.library.domain.entity.position.PaymentType.FULL_PAYMENT
import ru.modulkassa.payment.library.domain.entity.position.Position
import ru.modulkassa.payment.library.domain.entity.position.PositionType.PAYMENT
import ru.modulkassa.payment.library.domain.entity.position.TaxationMode.OSN
import ru.modulkassa.payment.library.domain.entity.position.VatTag.VAT_20
import java.math.BigDecimal

class PaymentPresenterTest {

    @Test
    fun CheckPaymentOptionsAndShow_ByDefault_ShowsDescriptionAndPositions() {
        val presenter = createPresenter()
        val view = mock<PaymentView>()
        presenter.attachView(view)

        val positions =
            listOf(Position("name", BigDecimal.TEN, BigDecimal.ONE, PAYMENT, OSN, FULL_PAYMENT, VAT_20))
        val options = PaymentOptions.createSbpOptions(
            orderId = "order-id",
            description = "description",
            positions = positions
        )
        presenter.checkPaymentOptionsAndShow(options)

        verify(view, times(1)).showDescription("description")
        verify(view, times(1)).showPositions(positions)
    }

    @Test
    fun CheckPaymentOptionsAndShow_EmptyPositions_NeverShowPositions() {
        val presenter = createPresenter()
        val view = mock<PaymentView>()
        presenter.attachView(view)

        val options = PaymentOptions.createSbpOptions(
            orderId = "order-id",
            description = "description",
            positions = emptyList()
        )
        presenter.checkPaymentOptionsAndShow(options)

        verify(view, never()).showPositions(any())
    }

    private fun createPresenter(): PaymentPresenter {
        return PaymentPresenter(mock())
    }

}