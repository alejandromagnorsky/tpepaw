package ar.edu.itba.it.paw.web.validator;

import org.joda.time.DateTime;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import ar.edu.itba.it.paw.web.command.FilterForm;

@Component
public class FilterFormValidator implements Validator {
	public boolean supports(Class<?> clazz) {
		return FilterForm.class.equals(clazz);
	}

	public void validate(Object target, Errors errors) {
		FilterForm form = (FilterForm) target;
		
		DateTime from = form.getDateFrom();
		DateTime to = form.getDateTo();
		
		if (to != null && from != null && !to.isAfter(from.toInstant())) {
			errors.rejectValue("from", "invalid");
			errors.rejectValue("to", "invalid");
		}
		
	}

}
