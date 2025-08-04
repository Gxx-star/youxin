package com.example.youxin.utils

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

/**
 * 密码加密工具类
 */
object KeyStoreUtils {
    private const val KEY_ALIAS = "youxin_password_key" // 密钥别名（自定义）
    private const val KEY_STORE_PROVIDER = "AndroidKeyStore" // 密钥存储位置为系统密钥库
    private const val TRANSFORMATION = "AES/GCM/NoPadding" // AES-GCM 模式（带认证）

    // 生成或获取密钥（首次使用生成，后续直接获取）
    private fun getSecretKey(): SecretKey {
        val keyStore = java.security.KeyStore.getInstance(KEY_STORE_PROVIDER)
        keyStore.load(null)

        // 密钥已存在，直接获取
        if (keyStore.containsAlias(KEY_ALIAS)) {
            val entry = keyStore.getEntry(KEY_ALIAS, null) as java.security.KeyStore.SecretKeyEntry
            return entry.secretKey
        }

        // 密钥不存在，生成新密钥
        val keyGenerator = KeyGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_AES,
            KEY_STORE_PROVIDER
        )
        val parameterSpec = KeyGenParameterSpec.Builder(
            KEY_ALIAS,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .setUserAuthenticationRequired(false) // 无需用户认证（如指纹）
            .build()

        keyGenerator.init(parameterSpec)
        return keyGenerator.generateKey()
    }

    // 加密方法（返回：IV + 密文 的组合字符串，用 ":" 分隔）
    fun encrypt(data: String): String {
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, getSecretKey())

        // IV（初始化向量）是随机生成的，需要与密文一起存储（用于解密）
        val iv = cipher.iv
        val encryptedData = cipher.doFinal(data.toByteArray(Charsets.UTF_8))

        // 拼接 IV 和 密文（IV 长度固定为 12 字节，AES-GCM 推荐）
        return "${iv.toHexString()}:${encryptedData.toHexString()}"
    }

    // 解密方法（传入 encrypt() 返回的字符串）
    fun decrypt(encryptedData: String): String {
        val parts = encryptedData.split(":")
        if (parts.size != 2) {
            throw IllegalArgumentException("Invalid encrypted data format")
        }

        val iv = parts[0].hexToByteArray()
        val cipherText = parts[1].hexToByteArray()

        val cipher = Cipher.getInstance(TRANSFORMATION)
        val gcmParameterSpec = GCMParameterSpec(128, iv) // 128位认证标签长度
        cipher.init(Cipher.DECRYPT_MODE, getSecretKey(), gcmParameterSpec)

        val decryptedData = cipher.doFinal(cipherText)
        return String(decryptedData, Charsets.UTF_8)
    }

    // 字节数组转十六进制字符串（便于存储）
    private fun ByteArray.toHexString(): String {
        return joinToString("") { "%02x".format(it) }
    }

    // 十六进制字符串转字节数组
    private fun String.hexToByteArray(): ByteArray {
        return chunked(2)
            .map { it.toInt(16).toByte() }
            .toByteArray()
    }
}