package ru.modulkassa.payment.library.network

import io.reactivex.rxjava3.core.Single
import retrofit2.http.Body
import retrofit2.http.POST
import ru.modulkassa.payment.library.network.dto.CreateSbpPaymentRequestDto
import ru.modulkassa.payment.library.network.dto.CreateSbpPaymentResponseDto

internal interface PaymentApi {

    @POST("api/v1/sbp_payment")
    fun createSbpPayment(
        @Body createSbpPaymentRequest: CreateSbpPaymentRequestDto
    ): Single<CreateSbpPaymentResponseDto>

}