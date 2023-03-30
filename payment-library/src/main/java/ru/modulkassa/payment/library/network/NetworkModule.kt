package ru.modulkassa.payment.library.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import ru.modulkassa.payment.library.BuildConfig
import java.util.concurrent.TimeUnit.SECONDS

internal object NetworkModule {

    private val logger = HttpLoggingInterceptor.Logger { message -> println(message) }
    private val logInterceptor = HttpLoggingInterceptor(logger)
        .apply {
            this.level = HttpLoggingInterceptor.Level.BODY
        }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(logInterceptor)
        .connectTimeout(30, SECONDS)
        .readTimeout(30, SECONDS)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.PAY_MODUL_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .build()

    val payApi: PaymentApi = retrofit.create(PaymentApi::class.java)

}