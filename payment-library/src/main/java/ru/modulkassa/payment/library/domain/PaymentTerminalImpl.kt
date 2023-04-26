package ru.modulkassa.payment.library.domain

import com.google.gson.Gson
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.core.SingleSource
import retrofit2.HttpException
import ru.modulkassa.payment.library.SettingsRepository
import ru.modulkassa.payment.library.domain.entity.PaymentOptions
import ru.modulkassa.payment.library.network.PaymentApi
import ru.modulkassa.payment.library.network.dto.ErrorResponseDto
import ru.modulkassa.payment.library.network.mapper.CreateSbpPaymentRequestMapper
import ru.modulkassa.payment.library.ui.ValidationException

internal class PaymentTerminalImpl(
    private val api: PaymentApi,
    private val gson: Gson,
    private val repository: SettingsRepository
) : PaymentTerminal {

    override fun createSbpPaymentLink(options: PaymentOptions): Single<String> {
        println("Инициируем запрос на создание СБП оплаты с параметрами $options")
        return Single.fromCallable {
            CreateSbpPaymentRequestMapper(gson, repository).toDto(options)
        }.flatMap {
            api.createSbpPayment(it)
                .map { it.sbpLink }
                .onErrorResumeNext { handleError(it) }
        }
    }

    private fun handleError(throwable: Throwable): SingleSource<out String> {
        return (throwable as? HttpException)?.response()?.errorBody()?.charStream()?.let { reader ->
            val errorResponse = gson.fromJson(reader, ErrorResponseDto::class.java)
            println("Произошла ошибка валидации полей при создании платежа: \"${errorResponse.message}\"")
            errorResponse.fieldErrors?.forEach { (field, errorDescription) ->
                println("Для поля $field ошибка: $errorDescription")
            }
            errorResponse.formErrors?.let { formErrors ->
                println(formErrors)
            }
            Single.error(ValidationException(causeMessage = errorResponse.message))
        } ?: Single.error(throwable)
    }
}