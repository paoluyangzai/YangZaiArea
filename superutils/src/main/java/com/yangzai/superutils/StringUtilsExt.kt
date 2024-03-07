package com.yangzai.superutils

import java.util.regex.Pattern

/**
 *@author 许阳
 *2024/3/7 16:04
 **/
/**
 * 截取数字
 * 从字符串的第一个数字开始的连续数字
 * @param
 * @return
 */
fun String.yzSplitNumbers(): String? {
    val pattern = Pattern.compile("\\d+")
    val matcher = pattern.matcher(this)
    while (matcher.find()) {
        return matcher.group(0)
    }
    return ""
}

/**
 * 截取非数字
 * 从字符串第一个非数字起的连续非数字
 * @param
 * @return
 */
fun String.yzSplitNotNumber(): String? {
    val pattern = Pattern.compile("\\D+")
    val matcher = pattern.matcher(this)
    while (matcher.find()) {
        return matcher.group(0)
    }
    return ""
}

/**
 * 判断是否包含数字
 *
 * @param str
 * @return true 包含， false 不包含
 */
fun String.yzIsIncludeDigit(): Boolean {
    var flag = false
    val p = Pattern.compile(".*\\d+.*")
    val m = p.matcher(this)
    if (m.matches()) {
        flag = true
    }
    return flag
}

