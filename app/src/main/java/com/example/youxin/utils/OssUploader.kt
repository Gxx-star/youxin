package com.example.youxin.utils

import android.net.Uri
import android.util.Log
import com.alibaba.sdk.android.oss.ClientConfiguration
import com.alibaba.sdk.android.oss.ClientException
import com.alibaba.sdk.android.oss.OSSClient
import com.alibaba.sdk.android.oss.ServiceException
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider
import com.alibaba.sdk.android.oss.common.auth.OSSStsTokenCredentialProvider
import com.alibaba.sdk.android.oss.model.ObjectMetadata
import com.alibaba.sdk.android.oss.model.PutObjectRequest
import com.alibaba.sdk.android.oss.model.PutObjectResult
import com.example.youxin.MyApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random

/**
 * 基于阿里云oss服务上传头像工具类
 * @param uri: 文件本地uri
 */
object OssUploader {
    suspend fun uploadFile(uri: Uri?) :String = withContext(Dispatchers.IO) {
        var endpoint = ""
        var accessKeyId = ""
        var accessKeySecret = ""
        var securityToken = ""
        val url = URL("http://114.215.194.88/oss.txt")
        val fileName = ImageNameGenerator.generateUniqueImageName()
        val connection = withContext(Dispatchers.IO) {
            url.openConnection()
        } as HttpURLConnection
        withContext(Dispatchers.IO) {
            connection.connect()
        }
        BufferedReader(InputStreamReader(connection.inputStream)).use { br ->
            var line:String? = null
            var count = 0
            while ((br.readLine().also { line = it }) != null) {
                when(count) {
                    0-> {endpoint = line!!}
                    1->{ accessKeyId = line!!}
                    2-> {accessKeySecret = line!!}
                    3-> {securityToken = line!!}
                }
                count++
            }
        }
        val region = "cn-beijing"

        val credentialProvider: OSSCredentialProvider =
            OSSStsTokenCredentialProvider(accessKeyId, accessKeySecret, securityToken)
        val config = ClientConfiguration()

        val oss = OSSClient(MyApplication.getContext(), endpoint, credentialProvider)
        oss.setRegion(region)

        val put = PutObjectRequest(
            "chan-xin",
            "chan_xin/image/$fileName",
            uri
        )
        val metadata = ObjectMetadata()

        metadata.setHeader("x-oss-object-acl", "public-read")
        metadata.setHeader("x-oss-storage-class", "Standard")
        put.metadata = metadata

        try {
            //oss.putObject(put)
            val putResult: PutObjectResult = oss.putObject(put)

            Log.d("PutObject", "UploadSuccess")
            Log.d("ETag", if(putResult.eTag != null) putResult.eTag else "null")
            Log.d("RequestId", if(putResult.requestId != null) putResult.requestId else "null")
            Log.d("myTag", "上传成功")
            return@withContext "https://chan-xin.oss-cn-beijing.aliyuncs.com/chan_xin/image/$fileName"
        } catch (e: ClientException) {
            // 客户端异常，例如网络异常等。
            Log.e("网络异常",e.toString())
            e.printStackTrace()
            return@withContext ""
        } catch (e: ServiceException) {
            // 服务端异常。
            Log.e("RequestId", e.requestId)
            Log.e("ErrorCode", e.errorCode)
            Log.e("HostId", e.hostId)
            Log.e("RawMessage", e.rawMessage)
            return@withContext ""
        }
    }
}

/**
 * 图片名生成工具类，用于生成不重复的图片文件名
 */
object ImageNameGenerator {
    // 时间格式：年月日时分秒毫秒
    private val dateFormat = SimpleDateFormat("yyyyMMddHHmmssSSS", Locale.getDefault())
    // 随机字符串的字符集
    private val charPool = ('a'..'z') + ('A'..'Z') + ('0'..'9')
    // 随机字符串的长度
    private const val RANDOM_STRING_LENGTH = 8

    /**
     * 生成不重复的图片名
     * @param extension 图片扩展名，不带点号，默认为"jpg"
     * @return 生成的唯一图片名，格式为"img_时间戳_随机字符串.扩展名"
     */
    fun generateUniqueImageName(extension: String = "jpg"): String {
        // 获取当前时间戳字符串
        val timestamp = dateFormat.format(Date())
        // 生成随机字符串
        val randomString = generateRandomString(RANDOM_STRING_LENGTH)
        // 拼接文件名
        return "img_${timestamp}_$randomString.${extension.lowercase()}"
    }

    /**
     * 生成指定长度的随机字符串
     * @param length 随机字符串的长度
     * @return 生成的随机字符串
     */
    private fun generateRandomString(length: Int): String {
        return (1..length)
            .map { Random.nextInt(0, charPool.size) }
            .map(charPool::get)
            .joinToString("")
    }
}
