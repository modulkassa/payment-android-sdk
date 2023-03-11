package ru.modulkassa.payment.library.network

import io.reactivex.rxjava3.core.Single
import retrofit2.http.POST

interface PaymentApi {

    // todo чисто для заглушки, будет переделано, когда будет апи

    @POST("api/sbp/qr-code")
    fun createPay(
        qrPayRequest: String
    ): Single<String>

}