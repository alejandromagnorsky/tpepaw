package ar.edu.itba.it.paw.web.formatter;

import java.util.Locale;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;
import org.springframework.format.Formatter;

public class DateFormatter implements Formatter<DateTime> {

	public String print(DateTime date, Locale locale) {
		return date.toString();
	}

	public DateTime parse(String pattern, Locale locale) {

		DateTimeFormatter formatter = new DateTimeFormatterBuilder()
				.appendDayOfMonth(2).appendLiteral('/').appendMonthOfYear(2)
				.appendLiteral('/').appendYear(4, 4).toFormatter();

		return formatter.parseDateTime(pattern);
	}
}
