package ar.edu.itba.it.paw.web.converter;

import java.util.Locale;

import org.apache.wicket.util.convert.ConversionException;
import org.apache.wicket.util.convert.IConverter;

import ar.edu.itba.it.paw.model.Time;

@SuppressWarnings("serial")
public class TimeConverter implements IConverter {

	public TimeConverter() {

	}

	public Object convertToObject(String time, Locale locale) {
		if(time == null || time.isEmpty())
			return null;
		if(!time.matches("(\\d+d)|(\\d+h)|(\\d+m)|(\\d+d \\d+h)|(\\d+d \\d+m)|(\\d+h \\d+m)|(\\d+d \\d+h \\d+m)"))
			throw new ConversionException("");
		int minutes = 0;
		String number;
		if (time.contains("d")){
			number = time.split("d")[0];
			if(number.contains(" "))
				number = number.substring(number.lastIndexOf(" ")).trim();
			minutes += 60*8*Integer.valueOf(number);
		} if(time.contains("h")){
			number = time.split("h")[0];
			if(number.contains(" "))
				number = number.substring(number.lastIndexOf(" ")).trim();
			minutes += 60*Integer.valueOf(number);
		} if(time.contains("m")){
			number = time.split("m")[0];
			if(number.contains(" "))
				number = number.substring(number.lastIndexOf(" ")).trim();
			minutes += Integer.valueOf(number);
		}
		return new Time(minutes);
	}

	public String convertToString(Object value, Locale locale) {
		Time time = (Time) value;
		if (time == null)
			return null;
		StringBuilder ans = new StringBuilder();
		if (time.getDays() != 0) {
			ans.append(time.getDays() + "d");
			if (time.getHourOfDay() != 0 || time.getMinuteOfHour() != 0)
				ans.append(" ");
		}
		if (time.getHourOfDay() != 0) {
			ans.append(time.getHourOfDay() + "h");
			if (time.getMinuteOfHour() != 0)
				ans.append(" ");
		}
		if (time.getMinuteOfHour() != 0)
			ans.append(time.getMinuteOfHour() + "m");
		return ans.toString();
	}
}
