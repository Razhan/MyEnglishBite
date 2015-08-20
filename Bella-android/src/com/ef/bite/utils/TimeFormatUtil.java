package com.ef.bite.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class TimeFormatUtil {

	public static String convertSecondsToHMmSs(long seconds) {
		seconds = seconds / 1000;
		long s = seconds % 60;
		long m = (seconds / 60) % 60;
		long h = (seconds / (60 * 60)) % 24;
		if (h == 0)
			return String.format("%02d:%02d", m, s);
		else
			return String.format("%02d:%02d:%02d", h, m, s);
	}

	/**
	 * 得到UTC时间，类型为字符串，格式为"yyyy-MM-dd HH:mm:ss"<br />
	 * 如果获取失败，返回null
	 * @return
	 */
	public static String getUTCTimeStr() {
		StringBuffer UTCTimeBuffer = new StringBuffer();
		// 1、取得本地时间：
		Calendar cal = Calendar.getInstance() ;
		// 2、取得时间偏移量：
		int zoneOffset = cal.get(java.util.Calendar.ZONE_OFFSET);
		// 3、取得夏令时差：
		int dstOffset = cal.get(java.util.Calendar.DST_OFFSET);
		// 4、从本地时间里扣除这些差量，即可以取得UTC时间：
		cal.add(java.util.Calendar.MILLISECOND, -(zoneOffset + dstOffset));
//		int year = cal.get(Calendar.YEAR);
//		int month = cal.get(Calendar.MONTH)+1;
//		int day = cal.get(Calendar.DAY_OF_MONTH);
//		int hour = cal.get(Calendar.HOUR_OF_DAY);
//		int minute = cal.get(Calendar.MINUTE);
//		int second = cal.get(Calendar.SECOND);

		DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd") ;
		DateFormat format2 = new SimpleDateFormat("HH:mm:ss") ;
		try{
			UTCTimeBuffer.append(format1.format(cal.getTime())).append("T").append(format2.format(cal.getTime())).append("Z");
			return UTCTimeBuffer.toString() ;
		}catch(Exception e)
		{
			e.printStackTrace() ;
		}
		return null ;
	}

	public static int utc2Local(String utcTime, int days) {
		int isSureys = 0;
		SimpleDateFormat utcFormater = new SimpleDateFormat(
				"yyyy-MM-dd'T'HH:mm:ss.SSSZ");
		utcFormater.setTimeZone(TimeZone.getTimeZone("UTC"));
		Date gpsUTCDate = null;
		try {
			gpsUTCDate = utcFormater.parse(utcTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		SimpleDateFormat localFormater = new SimpleDateFormat("yyyy-MM-dd");
		localFormater.setTimeZone(TimeZone.getDefault());
		String localTime = localFormater.format(gpsUTCDate.getTime());
		String nowTime = localFormater.format(new Date());
		return compare_date(localTime, nowTime, days);
	}

	public static int compare_date(String utcTime, String nowTime, int days) {

		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date utc = df.parse(utcTime);
			Date now = df.parse(nowTime);
			Calendar c = Calendar.getInstance();
			c.setTime(utc);
			c.add(c.DATE, days);
			Date temp_date = c.getTime();
			if (temp_date.getTime() > now.getTime()) {
				System.out.println("utc 在now前");
				return 1;
			} else if (temp_date.getTime() < now.getTime()) {
				System.out.println("utc在now后");
				return -1;
			} else {
				return 0;
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return 0;
	}

}
