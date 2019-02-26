package com.hj.tj.gohome.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 日期工具类。
 *
 */
public class DateUtil {

	private static Logger log = LoggerFactory.getLogger(DateUtil.class);
	/** 常规日期时间格式，24小时制yyyy-MM-dd HH:mm:ss **/
	public static final String NORMAL_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

	/** 常规日期，yyyy-MM-dd **/
	public static final String NORMAL_DATE_FORMAT = "yyyy-MM-dd";
	/** 无间隔日期格式，yyyyMMddHHmmssS **/
	public static final String NOSPLIT_DATE_FORMAT = "yyyyMMddHHmmssS";
	public static final String NOSPLIT_DATE_FORMAT_DAY = "yyyyMMdd";

	/** 外国的时间格式, dd/MM/yyyy HH:mm **/
	public static final String NATION_DATETIME_FORMAT = "dd/MM/yyyy HH:mm";

	public static final String NATION_DATE_FORMAT = "dd/MM/yy";

	public static final String SIMPLE_DATE_FORMAT = "MM.dd";

	public static final String SIMPLE_MONTH_DAY = "MMdd";

	public static final long ONE_DAY_MILLISECOND = 24 * 60 * 60 * 1000;
	public static final long ONE_HOUR_MILLSECOND = 60 * 60 * 1000;

	public static final String CARD_BUY_DATE_FORMAT = "dd MMM yyyy";

	public static final String SHOW_DATE_FORMAT = "yyyy/MM/dd";
	public static final String SHOW_DATE_DETAIL_FORMAT = "yyyy/MM/dd HH:mm:ss";
	public static final String SHOW_DATE_TO_MINUTE = "yyyy/MM/dd HH:mm";
	public static final String DAY_SPLIT_MONTH = "dd/MM HH:mm";

	// public static final DateFormat DATE_TIME_FORMAT = new
	// SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	/**
	 * 取得表示当天的时间对象
	 * 
	 * @return
	 */
	public static Date getCurrentDay() {
		Calendar ca = Calendar.getInstance();
		ca.set(Calendar.HOUR_OF_DAY, 0);
		ca.set(Calendar.MINUTE, 0);
		ca.set(Calendar.SECOND, 0);
		ca.set(Calendar.MILLISECOND, 0);
		return ca.getTime();
	}

	/**
	 * 获取昨日凌晨 无时分秒
	 * 
	 * @author lujiangui
	 * @dataTime: 2015年12月12日 下午2:15:55
	 * @return
	 */
	public static Date getYesterday2() {
		Calendar ca = Calendar.getInstance();
		ca.set(Calendar.DAY_OF_MONTH, ca.get(Calendar.DAY_OF_MONTH) - 1);
		ca.set(Calendar.HOUR_OF_DAY, 0);
		ca.set(Calendar.MINUTE, 0);
		ca.set(Calendar.SECOND, 0);
		ca.set(Calendar.MILLISECOND, 0);
		return ca.getTime();
	}

	public static long getDayOfHour(long time) {
		Calendar ca = Calendar.getInstance();
		ca.setTimeInMillis(time);
		ca.set(Calendar.MINUTE, 0);
		ca.set(Calendar.SECOND, 0);
		ca.set(Calendar.MILLISECOND, 0);
		return ca.getTimeInMillis();
	}

	public static long addDayOfHour(long time, int hour) {
		Calendar ca = Calendar.getInstance();
		ca.setTimeInMillis(time);
		ca.add(Calendar.HOUR_OF_DAY, hour);
		return ca.getTimeInMillis();
	}

	/**
	 * 解析简单格式的日期yyyy-MM-dd HH:mm字符串
	 * 
	 * @param simpleDateStr
	 * @return
	 */
	public static Date parseSimpleForMinute(String simpleDateStr) {
		if (StringUtil.isBlank(simpleDateStr))
			return null;
		try {
			DateFormat simpleDateFormatForMinute = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			return simpleDateFormatForMinute.parse(simpleDateStr);
		} catch (ParseException e) {
			log.error(e.getMessage(), e);
			return null;
		}
	}

	public static Date parseDate(String dateStr, String format) {
		if (StringUtil.isBlank(dateStr))
			return null;
		try {
			DateFormat simpleDateFormatForMinute = new SimpleDateFormat(format);
			return simpleDateFormatForMinute.parse(dateStr);
		} catch (ParseException e) {
			log.error(e.getMessage(), e);
			return null;
		}
	}

	/**
	 * 返回某个时间的"yyyy-MM-dd HH:mm"字符串
	 * 
	 * @param time
	 * @return
	 */
	public static String getTimeStringForMinute(Long time) {
		DateFormat simpleDateFormatForMinute = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return simpleDateFormatForMinute.format(new Date(time));
	}

	/**
	 * 解析简单格式yyyy-MM-dd HH:mm:ss的日期字符串
	 * 
	 * @param simpleDateStr
	 * @return
	 */
	public static Date parseSimple(String simpleDateStr) {
		DateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (StringUtil.isBlank(simpleDateStr))
			return null;
		try {
			return simpleDateFormat.parse(simpleDateStr);
		} catch (ParseException e) {
			log.error(e.getMessage(), e);
			return null;
		}
	}

	/**
	 * 返回某个时间的"yyyy-MM-dd HH:mm:ss"字符串
	 * 
	 * @param time
	 * @return
	 */
	public static String getTimeString(Long time) {
		final DateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return simpleDateFormat.format(new Date(time));
	}

	/**
	 * 返回日期的微秒数
	 * 
	 * @param date
	 * @return
	 */
	public static Long getTimeLong(Date date) {
		return date.getTime();
	}

	/**
	 * 获取当前的时间
	 *
	 * @return
	 */
	public static Date getCurrentTime() {
		return new Date();
	}

	/**
	 * 获取当前的时间
	 *
	 * @return
	 */
	public static String formatCurrentTime(String patten) {
		return formatDateToString(new Date(), patten);
	}

	/**
	 * 将时间按格式转换为字符串，日期为空时转换为空字符串
	 *
	 * @param date
	 * @param patten
	 * @return
	 */
	public static String formatDateToString(Date date, String patten) {
		if (null == date)
			return "";
		SimpleDateFormat sd = new SimpleDateFormat(patten);
		return sd.format(date);
	}

	/**
	 * Description: 把日期转换为字符串
	 * 
	 * @param date
	 * @return yyyy-MM-dd HH:mm:ss
	 */
	public static String DateToString(Date date) {
		if (date == null) {
			return "";
		}
		return DateToString(date, null);
	}

	/**
	 * Description: 把日期转换为字符串
	 * 
	 * @param date
	 * @param format
	 *            default:yyyy-MM-dd HH:mm:ss
	 * @return default:yyyy-MM-dd HH:mm:ss
	 */
	public static String DateToString(Date date, String format) {
		if (date == null) {
			return "";
		}
		if (StringUtil.isBlank(format)) {
			DateFormat DATE_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return DATE_TIME_FORMAT.format(date);
		} else {
			SimpleDateFormat dateFormat = new SimpleDateFormat(format);
			return dateFormat.format(date);
		}
	}

	/**
	 * 将时间按24小时制格式("yyyy-MM-dd HH:mm:ss")转换为字符串，日期为空时转换为空字符串
	 *
	 * @param date
	 * @return
	 */
	public static String formatDateNormal(Date date) {
		if (null == date)
			return "";
		SimpleDateFormat sd = new SimpleDateFormat(NORMAL_DATETIME_FORMAT);
		return sd.format(date);
	}

	/**
	 * 将时间按24小时制格式("yyyy-MM-dd HH:mm:ss")转换为字符串，日期为空时转换为空字符串
	 *
	 * @param date
	 * @return
	 */
	public static String formatDateNormal(Long date) {
		if (null == date)
			return "";
		SimpleDateFormat sd = new SimpleDateFormat(NORMAL_DATETIME_FORMAT);
		return sd.format(new Date(date));
	}

	/**
	 * 功能描述：将字符串按格式转换为时间，字符串为空时转换为null
	 *
	 * @param dateStr
	 *            时间字符串
	 * @param patten
	 *            格式
	 * @return
	 * @version 1.0.0
	 * @since 1.0.0 create on: 2012-5-2
	 */
	public static Date formatStrToDate(String dateStr, String patten) {
		if (null == dateStr || "".equals(dateStr))
			return null;
		SimpleDateFormat sd = new SimpleDateFormat(patten);
		try {
			Date date = sd.parse(dateStr);
			return date;
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * 将字符串按24小时制格式("yyyy-MM-dd HH:mm:ss")转换为时间
	 * 
	 * @param dateStr
	 *            时间字符串
	 * @return 转换成的时间，字符串为空时转换为null
	 */
	public static Date formatStrToNormalDate(String dateStr) {
		return formatStrToDate(dateStr, NORMAL_DATETIME_FORMAT);
	}

	/**
	 * 获得按时间字符time(格式 "HH:mm:ss")转换的日期date
	 * 
	 * @param date
	 * @param timeString
	 *            格式 "HH:mm:ss"
	 * @return 非法返回null
	 */
	public static Date getDateByTimeString(Date date, String timeString) {
		timeString = formatDateToString(date, String.format("yyyy-MM-dd %s", timeString));
		return formatStrToDate(timeString, NORMAL_DATETIME_FORMAT);
	}

	/**
	 * 功能描述：获取对应日期的开始时间
	 *
	 * @param date
	 * @return
	 */
	public static Date getDayStart(Date date) {
		String dateStartString = formatDateToString(date, "yyyy-MM-dd 00:00:00");
		return formatStrToDate(dateStartString, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 功能描述：获取对应日期的结束时间
	 *
	 * @param date
	 * @return
	 */
	public static Date getDayEnd(Date date) {
		String dateStartString = formatDateToString(date, "yyyy-MM-dd 23:59:59");
		return formatStrToDate(dateStartString, "yyyy-MM-dd HH:mm:ss");
	}

	public static Date getDay(Date date) {
		String dateStartString = formatDateToString(date, "yyyy-MM-dd 00:00:00");
		return formatStrToDate(dateStartString, "yyyy-MM-dd HH:mm:ss");
	}

	public static Date getODay(Date date) {
		String dateStartString = formatDateToString(date, "yyyy-MM-dd");
		return formatStrToDate(dateStartString, "yyyy-MM-dd");
	}

	/**
	 * 功能描述：获取昨天
	 *
	 * @return
	 * @version 1.0.0
	 * @since 1.0.0 create on: 2012-8-15
	 */
	public static Date getYesterday() {
		return getDayBefore(1);
	}

	/**
	 * 功能描述：获取前day天的日期
	 *
	 * @param day
	 * @return
	 * @version 1.0.0
	 * @since 1.0.0 create on: 2012-8-15
	 */
	public static Date getDayBefore(Integer day) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) - day);
		return calendar.getTime();
	}

	public static Date getDayBefore(Date date, Integer day) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) - day);
		return calendar.getTime();
	}

	public static Date getDayAfter(Date date, Integer day) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + day);
		return calendar.getTime();
	}

	public static Date getMonthBefore(Integer month) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		// calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - month);
		calendar.add(Calendar.MONTH, -month);
		return calendar.getTime();
	}

	/**
	 * 获取当前日期的几个月前的信息。
	 * 
	 * @param month
	 * @return
	 */
	public static Date getCurrentDateMonthBefore(Integer month) {
		Calendar calendar = Calendar.getInstance();
		// calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - month);
		calendar.add(Calendar.MONTH, -month);
		return calendar.getTime();
	}

	public static Date getDateMonthBefore(Date date, Integer month) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		// 跨年有问题??
		// calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - month);
		calendar.add(Calendar.MONTH, -month);
		return calendar.getTime();
	}

	public static Date getMonthBefore(Date date, Integer month) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		// 跨年有问题??
		// calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - month);
		calendar.add(Calendar.MONTH, -month);
		return calendar.getTime();
	}

	public static Date getMonthAfter(Date date, Integer month) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		// 跨年有问题??
		// calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + month);
		calendar.add(Calendar.MONTH, month);
		return calendar.getTime();
	}

	/***
	 *
	 * 功能描述：获得当月，如2012-08
	 *
	 * @return
	 * @version 1.0.0
	 * @since 1.0.0 create on: 2012-8-17
	 */
	public static String getMonth() {
		String dateStartString = formatDateToString(new Date(), "yyyy-MM");
		return dateStartString;
	}

	public static Date getLastMonth() {
		return getMonthBefore(1);
	}

	public static boolean isMonthStart(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.DAY_OF_MONTH) == 1;
	}

	public static boolean isMonthEnd(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, 1);
		return calendar.get(Calendar.DAY_OF_MONTH) == 1;
	}

	/**
	 * 功能描述：获取对应日期的月头
	 *
	 * @param date
	 * @return
	 * @version 1.0.0
	 * @since 1.0.0 create on: 2012-8-15
	 */
	public static Date getMonthStart(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	/**
	 * 功能描述：获取对应日期的月尾
	 *
	 * @param date
	 * @return
	 * @version 1.0.0
	 * @since 1.0.0 create on: 2012-8-15
	 */
	public static Date getMonthEnd(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1);
		calendar.set(Calendar.DAY_OF_MONTH, 0);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	/**
	 * 功能描述：获取对应日期的月头
	 *
	 * @param date
	 * @return
	 * @version 1.0.0
	 * @since 1.0.0 create on: 2012-8-15
	 */
	public static Date getPreviousWeekStart(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.WEEK_OF_YEAR, -1);
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	/**
	 * 功能描述：获取对应日期的月尾
	 *
	 * @param date
	 * @return
	 * @version 1.0.0
	 * @since 1.0.0 create on: 2012-8-15
	 */
	public static Date getPreviousWeekEnd(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		// calendar.add(Calendar.WEEK_OF_YEAR, -1);
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	public static int getMonthDiff(Date bigDate, Date smallDate) {
		if (bigDate.compareTo(smallDate) <= 0) {
			return -1;
		}
		Calendar calInstance = Calendar.getInstance();
		calInstance.setTime(bigDate);
		int year1 = calInstance.get(Calendar.YEAR);
		int month1 = calInstance.get(Calendar.MONTH);
		calInstance.setTime(smallDate);
		int year2 = calInstance.get(Calendar.YEAR);
		int month2 = calInstance.get(Calendar.MONTH);
		return (year1 - year2) * 12 + month1 - month2;
	}

	public static int getDayDiff(Date bigDate, Date smallDate) {
		if (bigDate.compareTo(smallDate) <= 0) {
			return -1;
		}
		Long diff = (bigDate.getTime() - smallDate.getTime()) / 86400000;
		return diff.intValue();
	}

	public static int getHourDiff(Date bigDate, Date smallDate) {
		if (bigDate.compareTo(smallDate) <= 0) {
			return -1;
		}
		Long diff = (bigDate.getTime() - smallDate.getTime()) / (60 * 60 * 1000);
		return diff.intValue();
	}

	public static int getMinuteDiff(Date bigDate, Date smallDate) {
		if (bigDate.compareTo(smallDate) <= 0) {
			return -1;
		}
		Long diff = (bigDate.getTime() - smallDate.getTime()) / (60 * 1000);
		return diff.intValue();
	}

	/**
	 * 返回某个时间的"yyyy-MM-dd 00:00:00"字符串
	 * 
	 * @param time
	 * @return
	 */
	public static String getTimeZeroString(Long time) {
		DateFormat simpleDateTimeZeroFormat = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
		return simpleDateTimeZeroFormat.format(new Date(time));
	}

	public static int compareDate(Date date1, Date date2) {
		if (date1.getTime() > date2.getTime()) {
			return 1;
		} else if (date1.getTime() < date2.getTime()) {
			return -1;
		} else {
			return 0;
		}
	}

	/**
	 * 日期相加
	 *
	 * @param date
	 *            日期
	 * @param day
	 *            天数
	 * @return 返回相加后的日期
	 */
	public static Date addDate(Date date, int day) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(getMillis(date) + ((long) day) * 24 * 3600 * 1000);
		return c.getTime();
	}

	/**
	 * 日期相减
	 *
	 * @param date
	 *            日期
	 * @param date1
	 *            日期
	 * @return 返回相减后的日期
	 */
	public static int diffDate(Date date, Date date1) {
		return (int) ((getMillis(date) - getMillis(date1)) / (24 * 3600 * 1000));
	}

	/**
	 * 返回毫秒
	 *
	 * @param date
	 *            日期
	 * @return 返回毫秒
	 */
	public static long getMillis(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.getTimeInMillis();
	}

	/***
	 * 判断checkDate时间是否在当前时间之前
	 * 
	 * @param checkDate
	 * @return
	 */
	public static boolean isNowBefore(Date checkDate) {

		Calendar checkCal = Calendar.getInstance(Locale.CHINA);
		Calendar nowCal = Calendar.getInstance(Locale.CHINA);

		checkCal.setTime(checkDate);
		nowCal.setTime(new Date());

		return nowCal.before(checkCal);
	}

	/***
	 * 判断checkDate时间是否在当前时间之后
	 * 
	 * @param checkDate
	 * @return
	 */
	public static boolean isNowAfter(Date checkDate) {

		Calendar checkCal = Calendar.getInstance(Locale.CHINA);
		Calendar nowCal = Calendar.getInstance(Locale.CHINA);

		checkCal.setTime(checkDate);
		nowCal.setTime(new Date());

		return nowCal.after(checkCal);
	}

	/***
	 * 判断checkDate时间是否在当前时间之后
	 * 
	 * @return
	 */
	public static boolean isNowBetween(Date beginDate, Date endDate) {

		Calendar beginCal = Calendar.getInstance(Locale.CHINA);
		Calendar nowCal = Calendar.getInstance(Locale.CHINA);
		Calendar endCal = Calendar.getInstance(Locale.CHINA);

		beginCal.setTime(beginDate);
		endCal.setTime(endDate);
		nowCal.setTime(new Date());

		return nowCal.after(beginCal) && nowCal.before(endCal);
	}

	/***
	 * 判断sourceDate时间是否在两个时间范围内
	 * 
	 * @return
	 */
	public static boolean isNowBetweenWithDate(Date beginDate, Date endDate, Date sourceDate) {

		Calendar beginCal = Calendar.getInstance(Locale.CHINA);
		Calendar nowCal = Calendar.getInstance(Locale.CHINA);
		Calendar endCal = Calendar.getInstance(Locale.CHINA);

		beginCal.setTime(beginDate);
		endCal.setTime(endDate);
		nowCal.setTime(sourceDate);

		return nowCal.after(beginCal) && nowCal.before(endCal);
	}

	/**
	 * 将字符串转换为日期（包括时间）
	 * 
	 *            "yyyy-MM-dd HH:mm:ss"格式的日期字符串
	 * @return 日期时间
	 */
	public static Date convertToDateTime(String dateTimeString) {
		try {
			DateFormat DATE_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return DATE_TIME_FORMAT.parse(dateTimeString);
		} catch (ParseException e) {
			return null;
		}
	}

	public static void main(String[] args) throws ParseException {
		int dayDiff = DateUtil.getDayDiff(formatStrToDate("2018-09-05", NORMAL_DATE_FORMAT), formatStrToDate("2018-08-31", NORMAL_DATE_FORMAT));
		System.out.println(dayDiff);
	}

	/**
	 * 获取当前时间距离今日凌晨：00：00：00之间的秒数 〈功能详细描述〉
	 *
	 * @return int 剩余秒数
	 */
	public static int getTodayLostSecounds() {
		return (int) ((getMillis(getDayEnd(new Date())) - getMillis(new Date())) / (1000));
	}

	/** 获取两个时间的时间查 如1天2小时30分钟 */
	public static Map<String, Long> getDatePoor(Date endDate, Date nowDate) {

		long nd = 1000 * 24 * 60 * 60;
		long nh = 1000 * 60 * 60;
		long nm = 1000 * 60;
		// long ns = 1000;
		// 获得两个时间的毫秒时间差异
		long diff = endDate.getTime() - nowDate.getTime();
		// 计算差多少天
		long day = diff / nd;
		// 计算差多少小时
		long hour = diff % nd / nh;
		// 计算差多少分钟
		long min = diff % nd % nh / nm;

		Map<String, Long> resultmap = new HashMap<String, Long>();
		resultmap.put("day", day);
		resultmap.put("hour", hour);
		resultmap.put("min", min);
		return resultmap;
	}

	/**
	 * 增加、减少指定时数
	 * 
	 * @param date
	 * @param hour
	 *            要增加的时数（减少则为 负数）
	 * @return
	 */
	public static Date addHour(Date date, int hour) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.HOUR, hour);
		return c.getTime();
	}

	/**
	 * 增加、减少指定时数
	 * 
	 * @param date
	 *            要增加的时数（减少则为 负数）
	 * @return
	 */
	public static Date addMinute(Date date, int minute) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MINUTE, minute);
		return c.getTime();
	}

	/**
	 * 判断日期是否在今天
	 * 
	 * @param needStr
	 * @return
	 */
	public static boolean isToday(String needStr) {
		if (Objects.isNull(needStr)) {
			return false;
		}
		Date needDate = DateUtil.formatStrToNormalDate(needStr);
		Date currentDate = new Date();// 当前日期
		SimpleDateFormat sft = new SimpleDateFormat("yyyyMMdd");// 日期格式化
		return sft.format(needDate).equals(sft.format(currentDate));
	}

	/**
	 * 获取当前分钟时间
	 * 
	 * @return
	 */
	public static long getCurrentMinuteTime() {
		return Calendar.getInstance().getTime().getTime() / 60000;
	}


	/**
	 * 获取当前分钟时间
	 *
	 * @return
	 */
	public static long getCurrentMillis() {
		return Calendar.getInstance().getTime().getTime();
	}


	/**
	 * 获取分钟数是多少时间之前
	 * 
	 * @param minute
	 * @return
	 */
	public static String getTimeAgo(int minute, Date date) {
		String str = "";

		if (minute > 60 * 24 * 3) {
			str = DateToString(date, "yyyy-MM-dd");
		} else if (minute > 60 * 24) {
			// 天前
			int days = minute / (60 * 24);
			str = days + "hari";
		} else if (minute > 60) {
			// 小时前
			int minutes = minute / 60;
			str = minutes + "jam";
		} else {
			// 分钟前
			str = minute + "menit";
		}

		return str;
	}

	/**
	 * 获取两个时间相差的秒数
	 * 
	 * @param bigDate
	 * @param minDate
	 * @return
	 */
	public static int getDiffSeconds(Date bigDate, Date minDate) {
		long bigDateMillis = getMillis(bigDate);
		long minDateMillis = getMillis(minDate);
		int diffSeconds = (int) ((bigDateMillis - minDateMillis) / 1000);

		return diffSeconds;
	}

	/**
	 * 获取指定时间的 HH:mm 形式字符串
	 * 
	 * @param date
	 * @return
	 */
	public static String getHourAndMinuteStr(Date date) {
		Calendar calendar = Calendar.getInstance();
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minutes = calendar.get(Calendar.MINUTE);
		return hour + ":" + minutes;
	}

	/**
	 * 获取某个时间之后
	 * 
	 * @param date
	 * @param days
	 * @param hours
	 * @param minutes
	 * @return
	 */
	public static Date getTimeIn(Date date, int days, int hours, int minutes) {
		return new Date(date.getTime() + (days * 24 * 3600 + hours * 3600 + minutes * 60) * 1000);
	}

	/**
	 * 获取距离某个时间前几个小时的时间
	 * 
	 * @param date
	 * @param hour
	 * @return
	 */
	public static Date getHourBefore(Date date, int hour) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) - hour);
		return calendar.getTime();
	}

	/**
	 * 获取今天的开始时间
	 * @return
	 */
	public static Date getTodayStart(){
		return getDayStart(Calendar.getInstance().getTime());
	}

	/**
	 * 获取今天的结束时间
	 * @return
	 */
	public static Date getTodayEnd(){
		return getDayEnd(Calendar.getInstance().getTime());
	}


	/**
	 * 获取今天的结束时间
	 * @return
	 */
	public static Date getNow(){
		return Calendar.getInstance().getTime();
	}


}
