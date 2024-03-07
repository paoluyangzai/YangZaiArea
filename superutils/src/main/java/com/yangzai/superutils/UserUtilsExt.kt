package com.yangzai.superutils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * 与用户有关的工具
 * 包含功能有手机号码脱敏、姓名脱敏
 * @author 许阳
 * 2024/3/7 14:07
 **/

/**
 * 手机号脱敏
 * 手机号字符串调用此方法
 * @return 屏蔽中间四位的的手机号，例如136****8454
 */
fun String.yzDesensitizedPhoneNumber() =
    this.replace(regex = "(\\w{3})\\w*(\\w{4})".toRegex(), replacement = "$1****$2")


/**
 * 用户姓名脱敏
 * 姓名字符串调用此方法
 * @return 脱敏后的姓名
 */
fun String.yzDesensitizedUsername(): String {
    if (this.isEmpty() || this.length == 1) {
        return "*"
    }
    val chars = this.toCharArray()
    for (i in 0 until chars.size - 1) {
        chars[i] = '*'
    }
    return String(chars)
}


/**
 * 判断是否为邮箱
 * 邮箱字符串调用此方法
 */
fun String.yzCheckEmail(): Boolean {
    val p =
        Pattern.compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$")
    var m = p.matcher(this)
    return m.matches()
}

/**
 * 判断是否为手机号码
 * 手机号字符串调用此方法
 */
fun String.yzCheckPhone(): Boolean {
    var p =
        Pattern.compile("^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\d{8}$")
    val m: Matcher = p.matcher(this)
    return m.matches()
}


/**
 * 验证身份证号有效性
 *
 * @param idCard:身份证号
 * @return true/false
 */
fun String.yzCheckIdcard(): Boolean {
    // 号码长度应为15位或18位
    if (this == null || this.length != 15 && this.length != 18) {
        return false
    }
    /**
     * 身份证省份编码
     * */
    var zoneNum: Map<Int, String> = hashMapOf(
        Pair(11, "北京"),
        Pair(12, "天津"),
        Pair(13, "河北"),
        Pair(14, "山西"),
        Pair(15, "内蒙古"),
        Pair(21, "辽宁"),
        Pair(22, "吉林"),
        Pair(23, "黑龙江"),
        Pair(31, "上海"),
        Pair(32, "江苏"),
        Pair(33, "浙江"),
        Pair(34, "安徽"),
        Pair(35, "福建"),
        Pair(36, "江西"),
        Pair(37, "山东"),
        Pair(41, "河南"),
        Pair(42, "湖北"),
        Pair(43, "湖南"),
        Pair(44, "广东"),
        Pair(45, "广西"),
        Pair(46, "海南"),
        Pair(50, "重庆"),
        Pair(51, "四川"),
        Pair(52, "贵州"),
        Pair(53, "云南"),
        Pair(54, "西藏"),
        Pair(61, "陕西"),
        Pair(62, "甘肃"),
        Pair(63, "青海"),
        Pair(64, "宁夏"),
        Pair(65, "新疆"),
        Pair(71, "台湾"),
        Pair(81, "香港"),
        Pair(82, "澳门"),
        Pair(91, "国外"),
    )

    /**
     * 加权因子wi
     */
    val POWER_LIST = intArrayOf(7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2)

    /**
     * 校验码
     */
    val PARITYBIT = intArrayOf(
        '1'.code,
        '0'.code,
        'X'.code,
        '9'.code,
        '8'.code,
        '7'.code,
        '6'.code,
        '5'.code,
        '4'.code,
        '3'.code,
        '2'.code
    )

    /**
     * 判断字符串是否为日期格式(合法)
     *
     * @param inDate:字符串时间
     * @return true/false
     */
    fun isValidDate(inDate: String?): Boolean {

        if (inDate == null) {
            return false
        }
        // 或yyyy-MM-dd
        val dataFormat = SimpleDateFormat("yyyyMMdd")
        if (inDate.trim { it <= ' ' }.length != dataFormat.toPattern().length) {
            return false
        }
        // 该方法用于设置Calendar严格解析字符串;默认为true,宽松解析
        dataFormat.isLenient = false
        try {
            dataFormat.parse(inDate.trim { it <= ' ' })
        } catch (e: ParseException) {
            return false
        }
        return true
    }
    // 校验区位码
    if (!zoneNum.containsKey(
            Integer.valueOf(
                this.substring(
                    0,
                    2
                )
            )
        )
    ) {
        return false
    }
    // 校验年份
    val year = if (this.length == 15) "19" + this.substring(6, 8) else this.substring(6, 10)
    val iyear = year.toInt()
    if (iyear < 1900 || iyear > Calendar.getInstance()[Calendar.YEAR]) {
        // 1900年的PASS，超过今年的PASS
        return false
    }
    // 校验月份
    val month = if (this.length == 15) this.substring(8, 10) else this.substring(10, 12)
    val imonth = month.toInt()
    if (imonth < 1 || imonth > 12) {
        return false
    }
    // 校验天数
    val day = if (this.length == 15) this.substring(10, 12) else this.substring(12, 14)
    val iday = day.toInt()
    if (iday < 1 || iday > 31) {
        return false
    }
    // 校验一个合法的年月日
    if (!isValidDate(year + month + day)) {
        return false
    }
    // 校验位数
    var power = 0
    val cs = this.uppercase(Locale.getDefault()).toCharArray()
    for (i in cs.indices) { // 循环比正则表达式更快
        if (i == cs.size - 1 && cs[i] == 'X') {
            break // 最后一位可以是X或者x
        }
        if (cs[i] < '0' || cs[i] > '9') {
            return false
        }
        if (i < cs.size - 1) {
            power += (cs[i].code - '0'.code) * POWER_LIST.get(
                i
            )
        }
    }
    // 校验“校验码”
    return if (this.length == 15) {
        true
    } else cs[cs.size - 1].code == PARITYBIT.get(power % 11)
}

/**
 * 判断密码是否合法
 */
fun String.yzCheckPassword(): Boolean {
    var p = Pattern.compile("([A-Za-z0-9]){6,16}")
    var m =
        p.matcher(this)
    return m.matches()
}