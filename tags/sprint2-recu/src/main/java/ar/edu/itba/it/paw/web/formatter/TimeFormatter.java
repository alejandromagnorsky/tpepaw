package ar.edu.itba.it.paw.web.formatter;

import java.text.ParseException;
import java.util.Locale;

import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import ar.edu.itba.it.paw.model.Time;

@Component
public class TimeFormatter implements Formatter<Time>{

	public String print(Time time, Locale l) {
		if(time == null)
			return null;
		StringBuilder ans = new StringBuilder();
		if(time.getDays() != 0){
			ans.append(time.getDays()+"d");
			if(time.getHourOfDay() != 0 || time.getMinuteOfHour() != 0)
				ans.append(" ");
		}
		if(time.getHourOfDay() != 0){
			ans.append(time.getHourOfDay()+"h");
			if(time.getMinuteOfHour() != 0)
				ans.append(" ");
		}
		if(time.getMinuteOfHour() != 0)
			ans.append(time.getMinuteOfHour()+"m");
		return ans.toString();			
	}

	public Time parse(String time, Locale l) throws ParseException {
		if(time == null)
			return null;
		if(!time.matches("(\\d+d)|(\\d+h)|(\\d+m)|(\\d+d \\d+h)|(\\d+d \\d+m)|(\\d+h \\d+m)|(\\d+d \\d+h \\d+m)"))
			throw new IllegalArgumentException();
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
}
