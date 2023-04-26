package ru.modulkassa.payment.library

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys

internal class DefaultSettingsRepository(
    context: Context,
) : SettingsRepository {

    companion object {
        private const val KEY_MERCHANT_ID = "merchant_id"
        private const val KEY_SIGNATURE_KEY = "signature_key"
    }

    private val sharedPreferences = EncryptedSharedPreferences.create(
        "sensitive",
        MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    override fun save(merchantId: String, signatureKey: String) {
        sharedPreferences.edit()
            .putString(KEY_MERCHANT_ID, merchantId)
            .putString(KEY_SIGNATURE_KEY, signatureKey)
            .apply()
    }

    override fun getMerchantId(): String? {
        return sharedPreferences.getString(KEY_MERCHANT_ID, null)
    }

    override fun getSignatureKey(): String? {
        return sharedPreferences.getString(KEY_SIGNATURE_KEY, null)
    }
}