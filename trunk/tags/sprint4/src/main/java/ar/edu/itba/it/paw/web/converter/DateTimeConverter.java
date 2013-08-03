package ar.edu.itba.it.paw.web.converter;

import java.util.Locale;

import org.apache.wicket.util.convert.ConversionException;
import org.apache.wicket.util.convert.IConverter;
import org.joda.time.ReadableInstant;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.util.StringUtils;

@SuppressWarnings("serial")
public class DateTimeConverter implements IConverter {
	public static final String PATTERN = "dd/MM/yyyy";
	private static final DateTimeFormatter formatter = DateTimeFormat
			.forPattern(PATTERN);

	public Object convertToObject(String value, Locale locale) {
		try {
			return StringUtils.hasText(value) ? formatter.withLocale(locale)
					.parseDateTime(value) : null;
		} catch (IllegalArgumentException e) {
			throw new ConversionException("");
		}
	}

	public String convertToString(Object value, Locale locale) {
		return value == null ? null : formatter.withLocale(locale).print(
				(ReadableInstant) value);
	}
}