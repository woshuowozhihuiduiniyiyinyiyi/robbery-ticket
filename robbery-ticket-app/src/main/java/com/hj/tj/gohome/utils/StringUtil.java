package com.hj.tj.gohome.utils;

import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    public static final String YYYY_MM_DD_HH_mm_ss = "yyyy-MM-dd HH:mm:ss";
    public static final String YYYY_MM_DD_HH_mm_ss_fff = "yyyy-MM-dd HH:mm:ss.SSS";

    // 手机号正则
    public static String PHONE_REGEX = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(166)|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$";

    public static String format(String pattern, Date date) {
        return new SimpleDateFormat(pattern).format(date);
    }

    public static String formatDate24Time(Date date) {
        return new SimpleDateFormat(YYYY_MM_DD_HH_mm_ss).format(date);
    }

    public static String formatDate12Time(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(date);
    }

    public static String format24Time(Date date) {
        return new SimpleDateFormat("HH:mm:ss").format(date);
    }

    public static String format12Time(Date date) {
        return new SimpleDateFormat("hh:mm:ss").format(date);
    }

    public static String formatDate(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }

    public static Date parse(String pattern, String dateStr) {
        try {
            return new SimpleDateFormat(pattern).parse(dateStr);
        } catch (ParseException e) {

        }
        return null;
    }

    public static Date parseDate24Time(String dateStr) {
        try {
            return new SimpleDateFormat(YYYY_MM_DD_HH_mm_ss).parse(dateStr);
        } catch (ParseException e) {

        }
        return null;
    }

    public static Date parseDate12Time(String dateStr) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(dateStr);
        } catch (ParseException e) {

        }
        return null;
    }

    public static Date parseDate(String dateStr) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
        } catch (ParseException e) {

        }
        return null;
    }

    public static boolean isBlank(String str) {
        return str == null || "".equals(str.trim());
    }

    public static boolean isBlank(Integer str) {
        return str == null || str <= 0;
    }

    public static boolean isBlankx(Integer str) {
        return str == null || str < 0;
    }

    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    public static String fillBlankIfNull(String str) {
        if (str == null) {
            return "";
        } else {
            return str.trim();
        }
    }

    public static String fillBlankIfNull(Object str) {
        if (str == null) {
            return "";
        } else {
            return str.toString().trim();
        }
    }

    public static boolean isValid(String str, String pattern) {
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    public static boolean isNotValid(String str, String pattern) {
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(str);
        return !m.matches();
    }

    public static boolean isNotValidDate(String str) {
        String eL = "[0-9]{4}-[0-9]{2}-[0-9]{2}";
        Pattern p = Pattern.compile(eL);
        Matcher m = p.matcher(str);
        return !m.matches();
    }

    public static String trim(String str) {
        return str == null ? "" : str.trim();
    }

    public static String firstSub(String str, int length) {
        String result = "";
        if (str != null && !str.trim().equals("")) {
            if (str.length() <= length) {
                result = str;
            } else {
                result = str.substring(0, length);
            }
        }
        return result;
    }

    /**
     * 根据正则表达式截取字符串中的相应的字符, eg: msg：<service>sso</service> pattern:
     * <service>(.*)</service>
     *
     * @param msg
     * @param pattern
     * @return
     */
    public static String extract(String msg, String pattern) {
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(msg);
        if (m.find() && m.groupCount() > 0) {
            String result = m.group(1);
            return result == null ? "" : result.trim();
        } else {
            return "";
        }
    }

    /**
     * 判断是否中文字符
     *
     * @return boolean
     */
    public static boolean isChinese(char a) {
        int v = (int) a;
        return (v >= 19968 && v <= 171941);
    }

    /**
     * 判断是否含中文
     *
     * @return boolean
     */
    public static boolean chontainsChinese(String s) {
        if (null == s || "".equals(s.trim()))
            return false;
        for (int i = 0; i < s.length(); i++) {
            if (isChinese(s.charAt(i)))
                return true;
        }
        return false;
    }

    /**
     * 判断是否为工作日
     *
     * @return
     */
    public static boolean isWorkDay() {
        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_WEEK);
        return day >= 2 && day <= 6;
    }

    public static boolean isInvalidImei(String imei) {
        if (imei == null || imei.equals("-065535")) {
            return true;
        }

        Pattern p = Pattern.compile("^(0)*$");
        Matcher m = p.matcher(imei);
        return m.find();
    }


    /**
     * 屏蔽字符串中间部分
     *
     * @param str
     * @param maskStr
     * @return
     */
    public static String maskStrWithStr(String str, String maskStr) {
        if (StringUtil.isBlank(str)) {
            return "";
        }

        if (StringUtil.isBlank(maskStr)) {
            return str;
        }

        if (str.length() <= maskStr.length()) {
            return maskStr;
        }

        StringBuffer stringBuffer = new StringBuffer(str);
        Integer start = (stringBuffer.length() - maskStr.length()) / 2;
        str = stringBuffer.replace(start, start + maskStr.length(), maskStr).toString();
        return str;
    }

    /**
     * 判断source是否包含target这个词, 必须前后是分词的组成才能匹配成功，忽略大小写
     *
     * @param source
     * @param target
     * @return
     */
    public static boolean isMatchHoldWordEn(String source, String target) {
        if (StringUtils.isEmpty(target)) {
            return false;
        }
        Pattern pattern = Pattern.compile("(\\W|^)" + target.trim() + "(\\W|$)", Pattern.CASE_INSENSITIVE);
        return pattern.matcher(source).find();
    }

    public static String chineseNameMask(String name) {
        if (name.length() <= 1) {
            return "*";
        } else {
            return name.replaceAll("(.*)([\\u4e00-\\u9fa5]{1})", createAsterisk(name.length() - 1) + "$2");
        }
    }

    public static String phoneMask(String string) {
        int front = 3;
        int end = 2;
        return maskStringRemainPreAndSuffix(string, front, end);
    }

    /**
     * 用户身份证号码的打码隐藏加星号加*
     * <p>18位和非18位身份证处理均可成功处理</p>
     * <p>参数异常直接返回null</p>
     *
     * @param string 身份证号码
     * @return 处理完成的身份证
     */
    public static String idMask(String string) {
        int front = 1;
        int end = 1;
        return maskStringRemainPreAndSuffix(string, front, end);
    }

    /**
     * @param string
     * @param front  需要显示前几位 -->默认前1 后1
     * @param end    需要显示末几位
     * @return
     */
    public static String maskStringRemainPreAndSuffix(String string, int front, int end) {
        //身份证不能为空
        if (StringUtils.isEmpty(string)) {
            return null;
        }
        //需要截取的长度不能大于身份证号长度
        if ((front + end) > string.length()) {
            return null;
        }
        //需要截取的不能小于0
        if (front < 0 || end < 0) {
            return null;
        }
        //计算*的数量
        int asteriskCount = string.length() - (front + end);
        String asteriskStr = createAsterisk(asteriskCount);

        String regex = "(\\w{" + String.valueOf(front) + "})(\\w+)(\\w{" + String.valueOf(end) + "})";
        return string.replaceAll(regex, "$1" + asteriskStr + "$3");
    }

    public static String createAsterisk(int length) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < length; i++) {
            stringBuffer.append("*");
        }
        return stringBuffer.toString();
    }

    public static boolean isPhone(String phone) {
        if (phone.length() != 11) {
            return false;
        } else {
            Pattern p = Pattern.compile(PHONE_REGEX);
            Matcher m = p.matcher(phone);
            boolean isMatch = m.matches();
            return isMatch;
        }
    }


    /**
     * 粗糙校验是否是合法的身份证
     * @param idcard
     * @return
     */
//    public static boolean isValidIdCardNo(String idcard) {
//        if (StringUtils.isEmpty(idcard)) {
//            return false;
//        }
//        if (idcard.startsWith(Constants.COMMON_AUTH_ID_NUM_PREFIX)) {
//            return true;
//        }
//        String regex = "(^[1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$)|(^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}$)";
//        return Pattern.matches(regex, idcard);
//    }

}
