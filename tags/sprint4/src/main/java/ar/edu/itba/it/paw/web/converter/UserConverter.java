package ar.edu.itba.it.paw.web.converter;

import java.util.Locale;

import org.apache.wicket.util.convert.IConverter;

import ar.edu.itba.it.paw.model.User;

@SuppressWarnings("serial")
public class UserConverter implements IConverter {

	public UserConverter() {
	}

	public Object convertToObject(String value, Locale locale) {
		throw new RuntimeException("Operation not supported");
	}

	public String convertToString(Object value, Locale locale) {
		return ((User) value).getName();
	}

}
