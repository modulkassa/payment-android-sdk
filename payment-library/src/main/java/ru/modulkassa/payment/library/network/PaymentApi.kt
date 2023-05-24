package ru.modulkassa.payment.library.network

import io.reactivex.rxjava3.core.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import ru.modulkassa.payment.library.network.dto.SbpPaymentLinkRequestDto
import ru.modulkassa.payment.library.network.dto.SbpPaymentLinkResponseDto
import ru.modulkassa.payment.library.network.dto.TransactionResponseDto

internal interface PaymentApi {

    @POST("api/v1/sbp_payment")
    fun createSbpPayment(
        @Body createSbpPaymentRequest: SbpPaymentLinkRequestDto
    ): Single<SbpPaymentLinkResponseDto>

    @GET("api/v1/transaction-info")
    fun getTransaction(
        @Query("merchant") merchant: String,
        @Query("order_id") orderId: String,
        @Query("signature") signature: String,
        @Query("unix_timestamp") unixTimestamp: String,
        @Query("salt") salt: String
    ): Single<TransactionResponseDto>
}