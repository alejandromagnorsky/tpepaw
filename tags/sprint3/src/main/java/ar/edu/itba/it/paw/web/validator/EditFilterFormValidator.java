package ar.edu.itba.it.paw.web.validator;

import org.joda.time.DateTime;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import ar.edu.itba.it.paw.model.Filter;
import ar.edu.itba.it.paw.web.command.FilterForm;

@Component
public class EditFilterFormValidator implements Validator {
	
	
	public boolean supports(Class<?> clazz) {
		return FilterForm.class.equals(clazz);
	}

	public void validate(Object target, Errors errors) {
		FilterForm form = (FilterForm) target;
		
		String name = form.getName();
		if (name.isEmpty())
			errors.rejectValue("name", "required");
		
		Filter filter = form.getProject().getFilter(form.getName(), form.getUser());
		
		if (filter != null	&& !form.getOriginalFilter().getName().equals(name))
			errors.rejectValue("name", "existing");
		
		DateTime from = form.getDateFrom();
		DateTime to = form.getDateTo();
		
		if (to != null && from != null && !to.isAfter(from.toInstant())) {
			errors.rejectValue("from", "invalid");
			errors.rejectValue("to", "invalid");
		}
	}
}