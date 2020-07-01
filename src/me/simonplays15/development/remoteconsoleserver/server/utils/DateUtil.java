package me.simonplays15.development.remoteconsoleserver.server.utils;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateUtil {
	private static Pattern timePattern = Pattern.compile(
			"(?:([0-9]+)\\s*y[a-z]*[,\\s]*)?(?:([0-9]+)\\s*mo[a-z]*[,\\s]*)?(?:([0-9]+)\\s*w[a-z]*[,\\s]*)?(?:([0-9]+)\\s*d[a-z]*[,\\s]*)?(?:([0-9]+)\\s*h[a-z]*[,\\s]*)?(?:([0-9]+)\\s*m[a-z]*[,\\s]*)?(?:([0-9]+)\\s*(?:s[a-z]*)?)?",
			2);

	public static String removeTimePattern(String input) {
		return timePattern.matcher(input).replaceFirst("").trim();
	}

	public static long parseDateDiff(String time, boolean future) throws Exception {
		Matcher m = timePattern.matcher(time);
		int years = 0;
		int months = 0;
		int weeks = 0;
		int days = 0;
		int hours = 0;
		int minutes = 0;
		int seconds = 0;
		boolean found = false;
		while (m.find()) {
			if ((m.group() != null) && (!m.group().isEmpty())) {

				for (int i = 0; i < m.groupCount(); i++) {
					if ((m.group(i) != null) && (!m.group(i).isEmpty())) {
						found = true;
						break;
					}
				}
				if (found) {
					if ((m.group(1) != null) && (!m.group(1).isEmpty())) {
						years = Integer.parseInt(m.group(1));
					}
					if ((m.group(2) != null) && (!m.group(2).isEmpty())) {
						months = Integer.parseInt(m.group(2));
					}
					if ((m.group(3) != null) && (!m.group(3).isEmpty())) {
						weeks = Integer.parseInt(m.group(3));
					}
					if ((m.group(4) != null) && (!m.group(4).isEmpty())) {
						days = Integer.parseInt(m.group(4));
					}
					if ((m.group(5) != null) && (!m.group(5).isEmpty())) {
						hours = Integer.parseInt(m.group(5));
					}
					if ((m.group(6) != null) && (!m.group(6).isEmpty())) {
						minutes = Integer.parseInt(m.group(6));
					}
					if ((m.group(7) != null) && (!m.group(7).isEmpty())) {
						seconds = Integer.parseInt(m.group(7));
					}
				}
			}
		}
		if (!found) {
			throw new IllegalArgumentException("illegalDate");
		}
		Calendar c = new GregorianCalendar();
		if (years > 0) {
			c.add(1, years);// * (future ? 1 : -1));
		}
		if (months > 0) {
			c.add(2, months);// * (future ? 1 : -1));
		}
		if (weeks > 0) {
			c.add(3, weeks);// * (future ? 1 : -1));
		}
		if (days > 0) {
			c.add(5, days);// * (future ? 1 : -1));
		}
		if (hours > 0) {
			c.add(11, hours);// * (future ? 1 : -1));
		}
		if (minutes > 0) {
			c.add(12, minutes);// * (future ? 1 : -1));
		}
		if (seconds > 0) {
			c.add(13, seconds);// * (future ? 1 : -1));
		}
		Calendar max = new GregorianCalendar();
		max.add(1, 100);
		if (c.after(max)) {
			return max.getTimeInMillis();
		}
		return c.getTimeInMillis();
	}

	static int dateDiff(int type, Calendar fromDate, Calendar toDate, boolean future) {
		int diff = 0;
		long savedDate = fromDate.getTimeInMillis();
		while (((future) && (!fromDate.after(toDate))) || ((!future) && (!fromDate.before(toDate)))) {
			savedDate = fromDate.getTimeInMillis();
			fromDate.add(type, future ? 1 : -1);
			diff++;
		}
		diff--;
		fromDate.setTimeInMillis(savedDate);
		return diff;
	}

	public static String formatDateDiff(long date) {
		Calendar c = new GregorianCalendar();
		c.setTimeInMillis(date);
		Calendar now = new GregorianCalendar();
		return formatDateDiff(now, c);
	}

	public static String formatDateDiff(Calendar fromDate, Calendar toDate) {
		boolean future = false;
		if (toDate.equals(fromDate)) {
			return "now";
		}
		if (toDate.after(fromDate)) {
			future = true;
		}
		StringBuilder sb = new StringBuilder();
		int[] types = { 1, 2, 5, 11, 12, 13 };

		String[] names = { "year", "years", "month", "months", "day", "days", "hour", "hours", "minute", "minutes",
				"second", "seconds" };

		int accuracy = 0;
		for (int i = 0; i < types.length; i++) {
			if (accuracy > 2) {
				break;
			}

			int diff = dateDiff(types[i], fromDate, toDate, future);
			if (diff > 0) {
				accuracy++;
				sb.append(" ").append(diff).append(" ").append(names[(i * 2 + 0)]);
			}
		}
		if (sb.length() == 0) {
			return "now";
		}
		return sb.toString().trim();
	}
	
	public static String dateTimeToString(long millis) {
		if(millis <= 0) {
			return "forever";
		}
		long seconds = millis / 1000L;
		long minutes = 0L;
		while (seconds >= 60L) {
			seconds -= 60L;
			minutes += 1L;
		}
		long hours = 0L;
		while (minutes >= 60L) {
			minutes -= 60L;
			hours += 1L;
		}
		
		long days = 0L;
		while(hours >= 24L) {
			hours -= 24L;
			days += 1L;
		}
		
		return  "" + (days != 0 ? days + "d" : "") + "" + (hours != 0 ? hours + "h" : "") + (minutes != 0 ?  minutes + "m" : "") + (seconds != 0 ? seconds + "s" : "");
	}
	
}
