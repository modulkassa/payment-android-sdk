package ru.modulkassa.payment.library.domain

import com.google.gson.Gson
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.core.SingleSource
import retrofit2.HttpException
import ru.modulkassa.payment.library.R
import ru.modulkassa.payment.library.SettingsRepository
import ru.modulkassa.payment.library.domain.entity.PaymentOptions
import ru.modulkassa.payment.library.domain.entity.result.PaymentResultSuccess
import ru.modulkassa.payment.library.network.PaymentApi
import ru.modulkassa.payment.library.network.SignatureGenerator
import ru.modulkassa.payment.library.network.dto.BaseRequestDto
import ru.modulkassa.payment.library.network.dto.BaseResponseDto
import ru.modulkassa.payment.library.network.dto.BaseResponseStatus.ERROR
import ru.modulkassa.payment.library.network.dto.ErrorResponseDto
import ru.modulkassa.payment.library.network.dto.SbpPaymentLinkRequestDto
import ru.modulkassa.payment.library.network.dto.TransactionRequestDto
import ru.modulkassa.payment.library.network.dto.TransactionStateDto
import ru.modulkassa.payment.library.network.mapper.SbpPaymentLinkRequestMapper
import ru.modulkassa.payment.library.ui.NetworkException
import ru.modulkassa.payment.library.ui.PaymentFailedException
import ru.modulkassa.payment.library.ui.ValidationException
import java.util.concurrent.TimeUnit

internal class PaymentTerminalImpl(
    private val api: PaymentApi,
    private val gson: Gson,
    private val repository: SettingsRepository
) : PaymentTerminal {

    override fun createSbpPaymentLink(options: PaymentOptions): Single<String> {
        println("Инициируем запрос на создание СБП оплаты с параметрами $options")
        return Single.fromCallable {
            SbpPaymentLinkRequestMapper(gson, repository).toDto(options)
        }.map {
            signRequest(it) as SbpPaymentLinkRequestDto
        }.flatMap {
            api.createSbpPayment(it)
                .doOnSuccess { checkOnErrorStatus(it) }
                .map { it.sbpLink }
                .onErrorResumeNext { handleNetworkError(it) as SingleSource<out String> }
        }
    }

    // todo SDK-22 Проверить метод апи getTransaction(), когда сервер сделает
    override fun getPaymentStatus(orderId: String): Single<PaymentResultSuccess> {
        println("Начинаем проверку статуса платежа с orderId=${orderId}")
        return Single.fromCallable {
            val merchant = repository.getMerchantId()
                ?: throw ValidationException(causeResource = R.string.error_validation_no_merchant_id)
            TransactionRequestDto(
                merchant = merchant,
                orderId = orderId
            )
        }.map {
            signRequest(it) as TransactionRequestDto
        }.flatMap { request ->
            api.getTransaction(
                merchant = request.merchant,
                orderId = request.orderId,
                signature = request.signature ?: "",
                salt = request.salt,
                unixTimestamp = request.unixTimestamp
            ).doOnSuccess { checkOnErrorStatus(it) }
                .map { it.transaction }
                .repeatWhen { it.delay(2, TimeUnit.SECONDS) }
                .takeUntil { it.state in listOf(TransactionStateDto.COMPLETE, TransactionStateDto.FAILED) }
                .filter { it.state in listOf(TransactionStateDto.COMPLETE, TransactionStateDto.FAILED) }
                .firstOrError()
                .map {
                    when (it.state) {
                        TransactionStateDto.COMPLETE -> {
                            PaymentResultSuccess(
                                transactionId = it.transactionId ?: "",
                                sbpTransactionId = it.sbpTransactionId ?: ""
                            )
                        }
                        else -> {
                            throw PaymentFailedException(it.message)
                        }
                    }
                }
                .timeout(10, TimeUnit.SECONDS)
                .onErrorResumeNext { handleNetworkError(it) as SingleSource<PaymentResultSuccess> }
        }
    }

    private fun signRequest(request: BaseRequestDto): BaseRequestDto {
        val signatureKey = repository.getSignatureKey()
            ?: throw ValidationException(causeResource = R.string.error_validation_no_signature_key)
        val generatedSignature = SignatureGenerator(gson).generate(request, signatureKey)
        request.signature = generatedSignature
        return request
    }

    private fun handleNetworkError(throwable: Throwable): Any {
        println("handleNetworkError ${throwable} ${throwable.message}")
        return (throwable as? HttpException)?.response()?.errorBody()?.charStream()?.let { reader ->
            val errorResponse = gson.fromJson(reader, ErrorResponseDto::class.java)
            println("Произошла ошибка валидации полей: \"${errorResponse.message}\"")
            errorResponse.fieldErrors?.forEach { (field, errorDescription) ->
                println("Для поля $field ошибка: $errorDescription")
            }
            errorResponse.formErrors?.let { formErrors ->
                println(formErrors)
            }
            Single.error<Any>(NetworkException(causeMessage = errorResponse.message))
        } ?: Single.error<Any>(throwable)
    }

    private fun checkOnErrorStatus(response: BaseResponseDto) {
        if (response.status == ERROR) {
            throw NetworkException(causeMessage = response.message)
        }
    }
}