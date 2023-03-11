package ru.modulkassa.payment.library.network

import android.util.Log
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import ru.modulkassa.payment.library.entity.GsonFactory
import java.util.concurrent.TimeUnit.SECONDS

internal class NetworkModule {

    private val logger = HttpLoggingInterceptor.Logger { message -> Log.d("HttpLoggingInterceptor", message) }
    private val logInterceptor = HttpLoggingInterceptor(logger)
        .apply {
            this.level = HttpLoggingInterceptor.Level.BODY
        }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(logInterceptor)
//        .addInterceptor(AuthorizationInterceptor(settingsRepository))
        .connectTimeout(30, SECONDS)
        .readTimeout(30, SECONDS)
        .build()

    private val gson: Gson = GsonFactory.provide()

    private val retrofit = Retrofit.Builder()
//      .baseUrl(BuildConfig.MODULKASSA_BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .build()

    val payApi: PaymentApi = retrofit.create(PaymentApi::class.java)

}