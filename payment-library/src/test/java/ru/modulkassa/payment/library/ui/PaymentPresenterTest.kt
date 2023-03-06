package ru.modulkassa.payment.library.ui

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import org.junit.Test
import ru.modulkassa.payment.library.entity.InventPosition
import ru.modulkassa.payment.library.entity.PaymentOptions
import java.math.BigDecimal

class PaymentPresenterTest {

    @Test
    fun CheckPaymentOptionsAndShow_ByDefault_ShowsDescriptionAndPositions() {
        val presenter = createPresenter()
        val view = mock<PaymentView>()
        presenter.attachView(view)

        val positions = listOf(InventPosition("name", BigDecimal.TEN, BigDecimal.ONE))
        val options = PaymentOptions(description = "description", inventPositions = positions)
        presenter.checkPaymentOptionsAndShow(options)

        verify(view, times(1)).showDescription("description")
        verify(view, times(1)).showPositions(positions)
    }

    @Test
    fun CheckPaymentOptionsAndShow_EmptyPositions_NeverShowPositions() {
        val presenter = createPresenter()
        val view = mock<PaymentView>()
        presenter.attachView(view)

        val options = PaymentOptions(description = "description", inventPositions = emptyList())
        presenter.checkPaymentOptionsAndShow(options)

        verify(view, never()).showPositions(any())
    }

    private fun createPresenter(): PaymentPresenter {
        return PaymentPresenter()
    }

}