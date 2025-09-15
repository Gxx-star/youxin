package com.example.youxin.utils

import android.text.TextUtils
import com.example.youxin.data.db.entity.ContactEntity
import net.sourceforge.pinyin4j.PinyinHelper
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

/**
 * 联系人按首字母分组工具类
 * 将List<ContactEntity>转换为按首字母分组的Map
 */
object ContactGroupUtil {

    /**
     * 将联系人列表按首字母分组
     * @param contacts 联系人列表
     * @return 分组后的Map，包含26个字母和一个#
     */
    fun groupContacts(contacts: List<ContactEntity>): Map<String, List<ContactEntity>> {
        // 初始化包含所有26个字母和#的Map
        val resultMap = initEmptyGroupMap()

        // 对联系人进行分组
        contacts.forEach { contact ->
            // 获取联系人昵称的首字母分组键
            val key = getFirstLetterKey(contact.nickName)
            // 将联系人添加到对应的分组中
            resultMap[key]?.add(contact)
        }

        // 对每个分组内的联系人按昵称排序
        resultMap.forEach { (key, list) ->
            list.sortBy { it.nickName.lowercase() }
        }

        // 返回排序后的Map（A-Z，#在最后）
        return sortGroupMap(resultMap)
    }

    /**
     * 初始化包含所有26个字母和#的空Map
     */
    private fun initEmptyGroupMap(): MutableMap<String, MutableList<ContactEntity>> {
        val map = HashMap<String, MutableList<ContactEntity>>()

        // 添加26个字母
        for (c in 'a'..'z') {
            map[c.toString()] = ArrayList()
        }

        // 添加#
        map["#"] = ArrayList()

        return map
    }

    /**
     * 对分组Map按字母顺序排序（A-Z，#在最后）
     */
    private fun sortGroupMap(map: Map<String, List<ContactEntity>>): Map<String, List<ContactEntity>> {
        return LinkedHashMap<String, List<ContactEntity>>().apply {
            // 先添加字母A-Z
            for (c in 'a'..'z') {
                val key = c.toString()
                map[key]?.let { put(key, it) }
            }
            // 最后添加#
            map["#"]?.let { put("#", it) }
        }
    }

    /**
     * 获取联系人昵称的首字母分组键
     * @param nickName 联系人昵称
     * @return 分组键（a-z或#）
     */
    fun getFirstLetterKey(nickName: String?): String {
        if (TextUtils.isEmpty(nickName)) {
            return "#"
        }

        // 获取第一个字符
        val firstChar = nickName!!.trim().first()

        // 处理字母
        if (firstChar in 'A'..'Z'|| firstChar in 'a'..'z') {
            return firstChar.lowercaseChar().toString()
        }

        // 处理汉字
        if (isChinese(firstChar)) {
            return getChineseFirstLetter(firstChar).lowercase()
        }

        // 其他字符都归为#
        return "#"
    }

    /**
     * 判断字符是否为汉字
     */
    private fun isChinese(c: Char): Boolean {
        return c.toInt() in 0x4E00..0x9FA5
    }

    /**
     * 获取汉字的拼音首字母
     */
    private fun getChineseFirstLetter(chineseChar: Char): String {
        val format = HanyuPinyinOutputFormat().apply {
            caseType = HanyuPinyinCaseType.UPPERCASE
            toneType = HanyuPinyinToneType.WITHOUT_TONE
        }

        return try {
            // 获取拼音，取第一个字母
            val pinyinArray = PinyinHelper.toHanyuPinyinStringArray(chineseChar, format)
            if (pinyinArray.isNullOrEmpty()) {
                "#"
            } else {
                pinyinArray[0].substring(0, 1)
            }
        } catch (e: BadHanyuPinyinOutputFormatCombination) {
            e.printStackTrace()
            "#"
        }
    }
}
