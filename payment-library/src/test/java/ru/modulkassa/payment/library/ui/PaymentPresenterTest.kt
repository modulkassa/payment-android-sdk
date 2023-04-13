package ru.modulkassa.payment.library.ui

class PaymentPresenterTest {

    // todo SDK-19 Написать тесты на PaymentPresenter
    // поддержать rx в тестах

//    @Test
//    fun CheckPaymentOptionsAndShow_ByDefault_ShowsDescriptionAndPositions() {
//        val presenter = createPresenter()
//        val view = mock<PaymentView>()
//        presenter.attachView(view)
//
//        val positions =
//            listOf(Position("name", BigDecimal.TEN, BigDecimal.ONE, OSN, PAYMENT, FULL_PAYMENT, VAT_20))
//        val options = PaymentOptions.createSbpOptions(
//            orderId = "order-id",
//            description = "description",
//            positions = positions
//        ).apply {
//            merchantId = "merchantId"
//            signatureKey = "signatureKey"
//        }
//        presenter.checkPaymentOptionsAndShow(options)
//
//        verify(view, times(1)).showDescription("description")
//        verify(view, times(1)).showPositions(positions)
//    }
//
//    @Test
//    fun CheckPaymentOptionsAndShow_EmptyPositions_NeverShowPositions() {
//        val presenter = createPresenter()
//        val view = mock<PaymentView>()
//        presenter.attachView(view)
//
//        val options = PaymentOptions.createSbpOptions(
//            orderId = "order-id",
//            description = "description",
//            positions = emptyList()
//        ).apply {
//            merchantId = "merchantId"
//            signatureKey = "signatureKey"
//        }
//        presenter.checkPaymentOptionsAndShow(options)
//
//        verify(view, never()).showPositions(any())
//    }
//
//    private fun createPresenter(): PaymentPresenter {
//        return PaymentPresenter(mock())
//    }

}