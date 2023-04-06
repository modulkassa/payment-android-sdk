package ru.modulkassa.payment.library.domain

import com.google.gson.Gson
import io.reactivex.rxjava3.core.Single
import ru.modulkassa.payment.library.domain.entity.PaymentOptions
import ru.modulkassa.payment.library.network.PaymentApi
import ru.modulkassa.payment.library.network.dto.BaseResponseStatus
import ru.modulkassa.payment.library.network.mapper.CreateSbpPaymentRequestMapper

internal class PaymentTerminalImpl(
    private val api: PaymentApi,
    private val gson: Gson
) : PaymentTerminal {

    override fun createSbpPaymentLink(options: PaymentOptions): Single<String> {
        println("Инициируем запрос на создание СБП оплаты с параметрами $options")
        return Single.fromCallable {
            CreateSbpPaymentRequestMapper(gson).toDto(options)
        }.flatMap {
            api.createSbpPayment(it)
                .map { response ->
                    if (response.status == BaseResponseStatus.OK) {
                        response.sbpLink ?: throw Throwable("Пустая sbpLink") // todo переделать SDK-9
                    } else {
                        // todo SDK-9 Продумать обработку ошибок из апи и rx цепочек
                        // залогировать все текста ошибок
                        throw Throwable(response.message)
                    }
                }
        }
    }
}

