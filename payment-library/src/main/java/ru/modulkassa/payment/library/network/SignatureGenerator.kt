package ru.modulkassa.payment.library.network

import android.util.Base64
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.modulkassa.payment.library.network.dto.CreateSbpPaymentRequestDto
import java.security.MessageDigest

/**
 * Генератор подписи для запроса создания СБП платежа
 * См подробнее https://modulbank.ru/support/algorithm_for_calculating_signature_field
 */
internal class SignatureGenerator(
    private val gson: Gson
) {
    fun generate(
        requestDto: CreateSbpPaymentRequestDto,
        secretKey: String
    ): String {

        // Первым шагом при вычислении поля signature является формирование строки,
        // которая содержит все значения непустых полей, передаваемые в запросе.
        // Эта строка имеет вид «field1=value1&field2=value2», в которой поля (строчными буквами)
        // и их значения перечислены в алфавитном порядке, а значения закодированы с помощью алгоритма base64

        val mapOfRequestDto = gson.fromJson<HashMap<String, String?>>(
            gson.toJson(requestDto),
            object : TypeToken<HashMap<String, String?>>() {}.type
        )
        val encodedRequestDto = mapOfRequestDto
            .filterKeys { it != "signature" }
            .filterValues { !it.isNullOrEmpty() }
            .toSortedMap()
            .mapValues { Base64.encodeToString(it.value?.toByteArray(), Base64.NO_WRAP) }
            .map { (key, value) -> "${key}=${value}" }
            .joinToString("&")

        // Затем от этой строки необходимо вычислить подпись с помощью хеш-функции SHA1,
        // следующим образом: «SHA1(secret_key + SHA1(secret_key + values)»

        val signature: String = sha1(secretKey + sha1(secretKey + encodedRequestDto))

        return signature
    }

    private fun sha1(source: String): String {
        return MessageDigest.getInstance("SHA-1")
            .digest(source.toByteArray())
            .joinToString("") { "%02x".format(it) }
    }
}